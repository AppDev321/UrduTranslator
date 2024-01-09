package com.core.customviews

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.android.inputmethod.latin.R
import com.android.inputmethod.latin.databinding.ProgressDialogViewBinding
import com.core.utils.Utils


class CustomProgressDialog constructor(
    private  val backgroundColor: Int = R.color.alert_dialog_progress_bg,
    private  val progressColor: Int = R.color.alert_dialog_progress
) {

    var dialog: CustomDialog? = null

    fun show(activity: Activity, title: CharSequence?): Dialog? {
        val viewBinding = ProgressDialogViewBinding.inflate(activity.layoutInflater)
        viewBinding.textViewProgressDescription.apply {
            title?.let { text = it }
        }
        viewBinding.cardViewBackground.setCardBackgroundColor(
            (ContextCompat.getColor(activity, backgroundColor))
        )

        setColorFilter(
            viewBinding.progressBar.indeterminateDrawable,
            (ContextCompat.getColor(activity, progressColor))
        )

        dialog = CustomDialog(activity)
        dialog?.setContentView(viewBinding.root)
        dialog?.show()
        return dialog
    }

    private fun setColorFilter(drawable: Drawable, color: Int) {
        if (Utils.androidQAndAbove) {
            drawable.colorFilter = BlendModeColorFilter(color, BlendMode.SRC_ATOP)
        } else {
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        }
    }

    class CustomDialog(context: Context) : Dialog(context, R.style.CustomDialogTheme) {
        init {
            window?.decorView?.rootView?.setBackgroundResource(R.color.transparency_20)
            window?.decorView?.setOnApplyWindowInsetsListener { _, insets ->
                insets.consumeSystemWindowInsets()
            }
            setCancelable(false)
        }
    }
}