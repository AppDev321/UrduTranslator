package com.dictionary.dialog

import android.view.View
import androidx.navigation.fragment.findNavController
import com.android.inputmethod.latin.R
import com.android.inputmethod.latin.databinding.DicBottomSheetImagePickerBinding
import com.core.base.BaseBottomSheetDialogFragment
import com.core.utils.setNavigationResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImageChooserDialog :
    BaseBottomSheetDialogFragment<DicBottomSheetImagePickerBinding>(
        DicBottomSheetImagePickerBinding::inflate
    ), View.OnClickListener {
    override fun onStart() {
        super.onStart()



    }
    companion object {
        const val PROFILE_PHOTO_SELECTION_OPTION = "profile_photo_selection_option"

        enum class SelectionType {
            GALLERY,
            CAMERA
        }
    }


    override fun initUserInterface(view: View?) {
        with(viewDataBinding) {
            btnOpenGallery.setOnClickListener(this@ImageChooserDialog)
            btnOpenCamera.setOnClickListener(this@ImageChooserDialog)
        }
    }


    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.btnOpenGallery -> setNavigationResult(PROFILE_PHOTO_SELECTION_OPTION, SelectionType.GALLERY.name)
            R.id.btnOpenCamera -> setNavigationResult(PROFILE_PHOTO_SELECTION_OPTION, SelectionType.CAMERA.name)
        }
        findNavController().popBackStack()
    }

}