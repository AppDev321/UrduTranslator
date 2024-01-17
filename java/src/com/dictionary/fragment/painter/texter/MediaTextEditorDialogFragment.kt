package com.dictionary.fragment.painter.texter

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.android.inputmethod.latin.R
import com.android.inputmethod.latin.databinding.DicDialogMediaAddTextBinding
import com.core.base.BaseDialogFragment
import com.core.extensions.TAG
import com.core.extensions.empty
import com.core.extensions.hideKeyboard
import com.core.extensions.showKeyboard
import com.core.utils.Utils.androidQAndAbove

class MediaTextEditorDialogFragment :
    BaseDialogFragment<DicDialogMediaAddTextBinding>(DicDialogMediaAddTextBinding::inflate) {

    //region VARIABLES

    companion object {
        const val EXTRA_INPUT_TEXT = "extra_input_text"
        const val EXTRA_COLOR_CODE = "extra_color_code"
    }

    private var colorCode = 0
    private var onTextDoneListener: ((inputText: String?, colorCode: Int) -> Unit)? = null

    //endregion


    //region LIFECYCLE

    override fun initUserInterface(view: View?) {

        colorCode = arguments?.getInt(EXTRA_COLOR_CODE) ?: 0

        viewDataBinding.editTextAddText.apply {
            setText(arguments?.getString(EXTRA_INPUT_TEXT))
            setTextColor(colorCode)
        }

        lifecycleScope.launchWhenResumed {
            viewDataBinding.editTextAddText.also {
                if (androidQAndAbove) {
                    it.postDelayed({
                        showKeyboard(it)
                    }, 300)
                } else {
                    showKeyboard(it)
                }
            }
        }

        viewDataBinding.verticalSlideColorPicker.setOnColorChangeListener { selectedColor ->
            colorCode = selectedColor
            viewDataBinding.editTextAddText.setTextColor(selectedColor)
        }

        viewDataBinding.layoutMediaAddText.setOnClickListener { onDone() }
    }

    override fun onStart() {
        super.onStart()
        dialog?.let {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            it.window!!.setLayout(width, height)
            it.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    fun setOnTextEditorListener(onTextDoneListener: ((inputText: String?, colorCode: Int) -> Unit)? = null) {
        this.onTextDoneListener = onTextDoneListener
    }

    //endregion


    //region UTIL

    fun show(
        @NonNull appCompatActivity: AppCompatActivity,
        inputText: String = String.empty,
        @ColorInt colorCode: Int = ContextCompat.getColor(appCompatActivity, R.color.white)
    ): MediaTextEditorDialogFragment {
        val args = Bundle()
        args.putString(EXTRA_INPUT_TEXT, inputText)
        args.putInt(EXTRA_COLOR_CODE, colorCode)
        val fragment = MediaTextEditorDialogFragment()
        fragment.arguments = args
        fragment.show(
            appCompatActivity.supportFragmentManager,
            MediaTextEditorDialogFragment.TAG
        )
        return fragment
    }

    private fun onDone() {
        this.hideKeyboard()
        this.dismiss()
        val inputText = viewDataBinding.editTextAddText.text.toString()
        onTextDoneListener?.takeIf { TextUtils.isEmpty(inputText).not() }
            ?.invoke(inputText, colorCode)
    }

    //endregion
}