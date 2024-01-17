package com.dictionary.fragment.painter

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.media.Image
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.android.inputmethod.latin.R
import com.android.inputmethod.latin.databinding.DicEditorFragmentBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.core.base.BaseFragment
import com.core.extensions.TAG
import com.core.utils.Utils
import com.core.utils.fileUtils.CacheHelper
import com.core.utils.fileUtils.FileUtils
import com.dictionary.activity.DetailActivity
import com.dictionary.fragment.painter.emoji.MediaEmojiEditorBSFragment
import com.dictionary.fragment.painter.texter.MediaTextEditorDialogFragment
import com.dictionary.utils.saveImageToGallery
import dagger.hilt.android.AndroidEntryPoint
import ja.burhanrashid52.photoeditor.PhotoEditor
import ja.burhanrashid52.photoeditor.SaveSettings
import java.io.File
import java.util.Date

@AndroidEntryPoint
class UrduEditorFragment :
    BaseFragment<DicEditorFragmentBinding>(DicEditorFragmentBinding::inflate) {

    companion object {
        const val IMAGEPATH = ""
    }

    lateinit var mPhotoEditor: PhotoEditor

    private var mSelectedColor = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        activity?.let {
            if (Utils.androidRAndAbove) {
                it.window.decorView.windowInsetsController?.hide(
                    android.view.WindowInsets.Type.statusBars()
                )
            } else {
                it.window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }
        }

        super.onCreate(savedInstanceState)
    }

    private val clickListener = View.OnClickListener { v ->
        when (v.id) {
            R.id.imageViewPhotoEditBack -> {
                activity?.onBackPressed()
            }

            R.id.imageViewPhotoEditUndo -> {
                mPhotoEditor.undo()
            }

            R.id.imageViewPhotoEditRedo -> {
                mPhotoEditor.redo()
            }

            R.id.imageViewPhotoEditEemoji -> {
                navigateToMediaAddEmoji()
            }

            R.id.imageViewPhotoEditText -> {
                showBrush(false)
                navigateToMediaAddText()
            }

            R.id.imageViewPhotoEditPaint -> {
                if (viewDataBinding.colorPickerView.visibility == View.VISIBLE) {
                    showBrush(false)
                } else {
                    showBrush(true)
                }
            }

            R.id.fabPhotoDone -> {
                saveImage("IMG_" + FileUtils.IMAGE_DATE_FORMAT.format(Date()) + ".jpg")
            }
        }
    }

    override fun onResume() {
        activity.let {
            it as DetailActivity
            it.supportActionBar?.hide()
        }
        super.onResume()
    }

    override fun initUserInterface(view: View?) {
        viewDataBinding.apply {
            mPhotoEditor = PhotoEditor.Builder(activity, photoEditorView)
                .setPinchTextScalable(true)
                .build()
            colorPickerView.setOnColorChangeListener { selectedColor ->
                mSelectedColor = selectedColor
                if (colorPickerView.visibility == View.VISIBLE) {
                    imageViewPhotoEditPaint.background =
                        getBackground(mSelectedColor)
                    mPhotoEditor.brushColor = selectedColor
                }
            }
            imageViewPhotoEditBack.setOnClickListener(clickListener)
            imageViewPhotoEditUndo.setOnClickListener(clickListener)
            imageViewPhotoEditRedo.setOnClickListener(clickListener)
            imageViewPhotoEditText.setOnClickListener(clickListener)
            imageViewPhotoEditPaint.setOnClickListener(clickListener)
            imageViewPhotoEditEemoji.setOnClickListener(clickListener)
            fabPhotoDone.setOnClickListener(clickListener)

            arguments?.getString(IMAGEPATH)?.let {
                loadImage(it)
            }
        }
    }

    fun getBackground(color: Int): GradientDrawable {
        val shape = GradientDrawable()
        shape.shape = GradientDrawable.OVAL
        shape.setColor(color)
        shape.setStroke(0, Color.BLACK)
        return shape
    }


    private fun saveImage(fileName: String) {

        val path =
            FileUtils.getOutputFilePath(
                CacheHelper.IMAGE_FOLDER_NAME,
                fileName,
                CacheHelper.PAINTED_IMAGE_EXTENSION
            )

        val saveSettings = SaveSettings.Builder()
            .setClearViewsEnabled(false)
            .setCompressFormat(Bitmap.CompressFormat.JPEG)
            .setCompressQuality(70)
            .setTransparencyEnabled(true)
            .build()

        mPhotoEditor.saveAsFile(path, saveSettings, object : PhotoEditor.OnSaveListener {
            override fun onSuccess(imagePath: String) {
                imagePath.takeIf {
                    it.contains(
                        getString(R.string.dic_app_name)
                    )
                }?.let { clearUnusedStorage(it) }
                onPaintSaveSuccess(imagePath)
            }

            override fun onFailure(exception: Exception) {
                onPainSaveFailure()
            }
        })

    }

    private val emojiSelectionListener: (emojiUnicode: String) -> Unit = { emojiUnicode: String ->
        mPhotoEditor.addEmoji(emojiUnicode)
    }

    private fun navigateToMediaAddEmoji() {
        activity?.let{
            MediaEmojiEditorBSFragment(emojiSelectionListener).apply {
                if (isAdded) {
                    return
                }
                show(it.supportFragmentManager, this.TAG)
            }
        }
    }

    fun clearUnusedStorage(filesPath: String) {
        File(filesPath).deleteMe()
    }


    private fun File?.deleteMe() {
        if (this?.exists() == true)
            delete()
    }

    fun onPaintSaveSuccess(filePath: String) {
        saveImageToGallery(filePath,requireContext()){
            if(it)
            {
                showToast("Image saved in gallery")
                activity?.onBackPressed()
            }
            else{
                showToast("Failed while saving image")
            }
        }
    }

    fun onPainSaveFailure() {
        showToast("Failed to save image")
    }

    private fun showBrush(enableBrush: Boolean) {
        if (enableBrush) {
            mPhotoEditor.brushColor = mSelectedColor
            viewDataBinding.imageViewPhotoEditPaint.background =
                getBackground(mSelectedColor)
            mPhotoEditor.setBrushDrawingMode(true)
            viewDataBinding.colorPickerView.visibility = View.VISIBLE
        } else {
            viewDataBinding.imageViewPhotoEditPaint.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.transparent
                )
            )
            mPhotoEditor.setBrushDrawingMode(false)
            viewDataBinding.colorPickerView.visibility = View.INVISIBLE
        }
    }

    private fun navigateToMediaAddText() {
        MediaTextEditorDialogFragment().show(activity as DetailActivity).apply {
            setOnTextEditorListener { inputText: String?, colorCode: Int ->
                mPhotoEditor.addText(inputText, colorCode)
            }
        }
    }


    private fun loadImage(filePath: String) {
        Glide.with(this)
            .asBitmap()
            .load(filePath)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    viewDataBinding.photoEditorView.source.setImageBitmap(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            })

    }

}