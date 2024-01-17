package com.dictionary.dialog

import android.view.View
import androidx.navigation.fragment.findNavController
import com.android.inputmethod.latin.R
import com.android.inputmethod.latin.databinding.DicBottomSheetImagePickerBinding
import com.core.base.BaseBottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImageChooserDialog :
    BaseBottomSheetDialogFragment<DicBottomSheetImagePickerBinding>(
        DicBottomSheetImagePickerBinding::inflate
    ), View.OnClickListener {

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

        val profileSelectionListener =
            findNavController().previousBackStackEntry?.savedStateHandle?.getLiveData<String>(
                PROFILE_PHOTO_SELECTION_OPTION
            )
        findNavController().popBackStack()
        when (v?.id) {
            R.id.btnOpenGallery -> profileSelectionListener?.value = SelectionType.GALLERY.name
            R.id.btnOpenCamera -> profileSelectionListener?.value = SelectionType.CAMERA.name
        }
    }

}