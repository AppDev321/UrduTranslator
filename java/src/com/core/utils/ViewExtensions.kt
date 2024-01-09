package com.core.utils

import android.content.Context
import android.graphics.Typeface
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.method.MovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.view.View
import android.view.View.TEXT_DIRECTION_LTR
import android.view.View.TEXT_DIRECTION_RTL
import android.view.ViewStub
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import androidx.core.content.ContextCompat
import com.android.inputmethod.latin.R

import com.core.extensions.empty
import com.core.interfaces.OnSingleClickListener

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import java.util.Locale

/**
 * Extension function to handle click listener for
 * a view of type kotlin Lambda click listener
 */
fun View.setOnSingleClickListener(l: (View) -> Unit) = setOnClickListener(OnSingleClickListener(l))

/**
 * Extension function to handle click listener on view
 */
fun View.setOnSingleClickListener(l: View.OnClickListener) =
    setOnClickListener(OnSingleClickListener(l))


/**
 * Applies text direction based on string content
 * **/
fun View.applyTextDirectionOnEditText(string: String = String.empty) {
    var keyboardLocal = string
    if (keyboardLocal.isEmpty())
        keyboardLocal = Locale.getDefault().displayName

    textDirection = if (isKeyboardRTL(keyboardLocal)) {
        TEXT_DIRECTION_RTL
    } else {
        TEXT_DIRECTION_LTR
    }
}


/**
 * Defines directionality based on first character of input string
 * @param string - input string
 * **/

fun isKeyboardRTL(string: String): Boolean {
    return try {
        val directionality = Character.getDirectionality(string[0])
        directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT ||
                directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC ||
                directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT_EMBEDDING ||
                directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT_OVERRIDE
    } catch (e: Exception) {
        false
    }
}


/**
 * Used ellipsize property and maxLines to avoid growing textView too long for lengthy texts. And
 * used some properties from layout to determine if text is grown than the limit.
 * Note: ellipsizedCount could be 0 even for long texts if all the text of that line in paragraph has
 * fitted properly in last line and there was a new line meant to be shown later on. So, we had to use
 * combination of ellipsizedCount and last character in last drawn line.
 */
fun TextView.readLargeText(
    longText: SpannableStringBuilder,
    movementMethod: MovementMethod = LinkMovementMethod.getInstance(),
    maxCharacters: Int =250,
    onTextTruncated: ((shortText: String) -> SpannableStringBuilder?)? = null,
    expandedLength: Int?,
    onClick: (expandedLength: Int) -> Unit
) {

    val textViewContent = this
    if (longText.length < maxCharacters || (expandedLength != null && expandedLength >= longText.length)) {
        textViewContent.text = longText
        textViewContent.movementMethod = movementMethod
    } else {

        val truncatedText = longText.substring(0, expandedLength?.takeIf { it < maxCharacters } ?: maxCharacters)
        var readMoreSpan: ClickableSpan? = null

        readMoreSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val nextLimit = length() + maxCharacters
                if (nextLimit < longText.length) {
                    val nextPageText = "${longText.substring(0, nextLimit)}... "
                    textViewContent.text = readMoreSpan?.let {
                        createReadMoreSpannable(
                            context, nextPageText, onTextTruncated,
                            it
                        )
                    } ?: longText
                } else {
                    textViewContent.text = longText
                }
                onClick(textViewContent.length())
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
                ds.color = ContextCompat.getColor(
                    this@readLargeText.context,
                    R.color.black
                )
            }
        }
        textViewContent.text =
            createReadMoreSpannable(context, truncatedText, onTextTruncated, readMoreSpan)
        textViewContent.movementMethod = movementMethod
    }
}


private fun createReadMoreSpannable(
    context: Context,
    truncatedText: String,
    onTextTruncated: ((shortText: String) -> SpannableStringBuilder?)? = null,
    readMoreSpan: ClickableSpan
): SpannableStringBuilder {

    val readMoreText = "Read More..."
    val spannableString = (onTextTruncated?.invoke(truncatedText) ?: SpannableStringBuilder(
        truncatedText
    )).append(" $readMoreText")
    spannableString.setSpan(
        readMoreSpan,
        truncatedText.length + 1,
        spannableString.length,
        SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    spannableString.setSpan(
        StyleSpan(Typeface.BOLD), truncatedText.length + 1,
        spannableString.length, SPAN_EXCLUSIVE_EXCLUSIVE
    )
    return spannableString
}


/**
 * To prevent crash when we try inflate view that was already inflated. Because OS delete ViewStub by inflating.
 */
fun ViewStub?.safeInflate() = if (this?.parent != null) inflate() else null

fun Group.setAlphaForAll(alpha: Float) = referencedIds.forEach {
    rootView.findViewById<View>(it).alpha = alpha
}