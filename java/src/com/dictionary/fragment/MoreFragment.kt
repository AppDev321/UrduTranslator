package com.dictionary.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.inputmethod.latin.R
import com.android.inputmethod.latin.databinding.DicMoreFragmentBinding
import com.android.inputmethod.latin.setup.SetupActivity
import com.core.base.BaseFragment
import com.core.extensions.showShortToast
import com.core.interfaces.ItemClickListener
import com.core.utils.PermissionUtilsNew
import com.dictionary.activity.DetailActivity
import com.dictionary.activity.MainActivity
import com.dictionary.adapter.SettingAdapter
import com.dictionary.dialog.ImageChooserDialog
import com.dictionary.dialog.ImageChooserDialog.Companion.PROFILE_PHOTO_SELECTION_OPTION
import com.dictionary.model.SettingsDataFactory
import com.dictionary.utils.CHOOSER
import com.dictionary.utils.getCameraOutputUri
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.theartofdev.edmodo.cropper.CropImage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MoreFragment : BaseFragment<DicMoreFragmentBinding>(DicMoreFragmentBinding::inflate),
    ItemClickListener {

    private lateinit var settingAdapter: SettingAdapter

    @Inject
    lateinit var settingsDataFactory: SettingsDataFactory

    override fun initUserInterface(view: View?) {
        settingAdapter = SettingAdapter(this)
        viewDataBinding.rec.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = settingAdapter
        }

        settingAdapter.setItems(settingsDataFactory.getMoreFragmentData())
    }

    override fun onItemClick(position: Int, view: View) {
        super.onItemClick(position, view)

        if (position == 0) {
           checkAudioPermission()
        } else {
            initBottomSheetListener()
            findNavController().navigate(R.id.action_show_image_choose_dialog)
        }

    }


    private fun initBottomSheetListener() {

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>(
            PROFILE_PHOTO_SELECTION_OPTION
        )?.observe(viewLifecycleOwner) { selection ->
            when (selection) {
                ImageChooserDialog.Companion.SelectionType.GALLERY.name -> {
                    requireActivity().apply {
                        com.dictionary.utils.checkPermission(
                            this,
                            CHOOSER.GALLERY,
                            this@MoreFragment,
                            pickImageFromGalleryForResult
                        )
                    }
                }

                else -> {
                    requireActivity().apply {
                        com.dictionary.utils.checkPermission(
                            this,
                            CHOOSER.CAMERA,
                            this@MoreFragment,
                            takePhotoForResult
                        )
                    }
                }

            }
        }
    }


    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                result.uri?.let {
                    passToEditorScreen(it)
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                result.error?.let {
                    activity?.showShortToast(it.message)
                }
            }
        }
    }

    private val pickImageFromGalleryForResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                CropImage.activity(it.data?.data)
                    .start(requireContext(), this)
                // it.data?.data?.let { it1 -> passToEditorScreen(it1) }
            }
        }

    private val takePhotoForResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                getCameraOutputUri(requireContext())?.apply {
                    CropImage.activity(this)
                        .start(requireContext(), this@MoreFragment)
                }
            }
        }

    private fun passToEditorScreen(uri: Uri) {
        val bundle = Bundle()
        bundle.putString(DetailActivity.IMAGEPATH, uri.toString())
        bundle.putInt(DetailActivity.DEFAULT_NAV_HOST_KEY, R.id.action_more_to_editor)
        findNavController().navigate(R.id.action_more_to_editor, bundle)
        findNavController().currentBackStackEntry?.savedStateHandle?.remove<String>(
            PROFILE_PHOTO_SELECTION_OPTION
        )
    }

    private fun checkAudioPermission()
    {
        if (PermissionUtilsNew.hasAudioPermission()) {
            activity?.let {
                it as MainActivity
                startActivity(Intent(it, SetupActivity::class.java))
            }
        } else {
            activity?.let {
                checkListOfPermission(
                    it,
                    listOf(Manifest.permission.RECORD_AUDIO)
                )
            }
        }
    }


    private fun checkListOfPermission(
        activity: Activity,
        permissions: Collection<String>
    ) {
        Dexter.withContext(activity)
            .withPermissions(permissions)
            .withListener(object : MultiplePermissionsListener {

                override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {

                    p0?.let {
                        if (it.areAllPermissionsGranted()) {
                            startActivity(Intent(activity, SetupActivity::class.java))

                        } else if (it.isAnyPermissionPermanentlyDenied) {
                            PermissionUtilsNew.showPermissionSettingsDialog(activity)
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?,
                    permissionToken: PermissionToken?
                ) {
                    permissionToken?.continuePermissionRequest()
                }
            })
            .check()
    }

}