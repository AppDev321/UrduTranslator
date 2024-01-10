package com.core.extensions

import android.app.Activity
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import com.android.inputmethod.latin.R


fun Context.queryCursor(
    uri: Uri,
    projection: Array<String>,
    selection: String? = null,
    selectionArgs: Array<String>? = null,
    sortOrder: String? = null,
    showErrors: Boolean = false,
    callback: (cursor: Cursor) -> Unit
) {
    try {
        val cursor = contentResolver.query(uri, projection, selection, selectionArgs, sortOrder)
        cursor?.use {
            if (cursor.moveToFirst()) {
                do {
                    callback(cursor)
                } while (cursor.moveToNext())
            }
        }
    } catch (e: Exception) {
        if (showErrors) {
            showErrorToast()
        }
    }
}

fun Context.showErrorToast(length: Int = Toast.LENGTH_LONG) {
    Toast.makeText(this, String.format(getString(R.string.an_error_occurred)), length).show()
}

fun Context.showShortToast(textRes: Int) {
    showShortToast(getString(textRes))
}

fun Context.showShortToast(text: String?) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

fun Context.showLongToast(textRes: Int) {
    showLongToast(getString(textRes))
}

fun Context.showLongToast(text: String?) {
    Toast.makeText(this, text, Toast.LENGTH_LONG).show()
}

fun Context.getDrawableCompat(@DrawableRes drawableRes: Int): Drawable? {
    return AppCompatResources.getDrawable(this, drawableRes)
}

fun Context.sendShareIntent(text: String) {
    startActivity(Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, text)
        type = "text/plain"
    })
}

fun Context.isActivityResultSuccess(resultCode: Int) = (resultCode == Activity.RESULT_OK)

fun Context.copyTextToClipboard(textToCopy: String) {
    val clipboardManager =
        applicationContext.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
    val clipData = ClipData.newPlainText("text", textToCopy)
    clipboardManager.setPrimaryClip(clipData)
    applicationContext.showShortToast("Text copied to clipboard")
}
