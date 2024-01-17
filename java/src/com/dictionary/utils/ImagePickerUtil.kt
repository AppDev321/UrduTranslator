package com.dictionary.utils

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.fragment.app.Fragment
import com.android.inputmethod.latin.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.core.BaseApplication
import com.core.utils.AppLogger
import com.core.utils.PermissionUtilsNew
import com.core.utils.fileUtils.FileUtils
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.io.File

private var TAG = "ProfileAvatarUtil"
const val REQUEST_CODE_SELECT_PIC: Int = 101
const val REQUEST_CODE_SELECT_CAMERA: Int = 102

enum class CHOOSER {
    CAMERA,
    GALLERY
}


fun checkPermission(activity: Activity, item: CHOOSER, fragment: Fragment) {
    when (item) {

        CHOOSER.CAMERA -> {
            if (PermissionUtilsNew.hasCameraPermission()) {
                openCamera(fragment)
            } else {
                checkListOfPermission(activity, listOf(Manifest.permission.CAMERA), item, fragment)
            }
        }

        CHOOSER.GALLERY -> {
            if (PermissionUtilsNew.hasStoragePermission(true)) {
                openAlbum(fragment)
            } else {
                checkListOfPermission(
                    activity,
                    PermissionUtilsNew.getMediaStoragePermissions().toList(),
                    item,
                    fragment
                )
            }
        }
    }
}

fun checkPermission(
    activity: Activity,
    item: CHOOSER,
    fragment: Fragment,
    result: ActivityResultLauncher<Intent>
) {
    when (item) {

        CHOOSER.CAMERA -> {
            if (PermissionUtilsNew.hasCameraPermission()) {
                openCamera(fragment, result)
            } else {
                checkListOfPermission(
                    activity,
                    listOf(Manifest.permission.CAMERA),
                    item,
                    fragment,
                    result
                )
            }
        }

        CHOOSER.GALLERY -> {
            if (PermissionUtilsNew.hasStoragePermission(true)) {
                openAlbum(fragment, result)
            } else {
                checkListOfPermission(
                    activity,
                    PermissionUtilsNew.getMediaStoragePermissions().toList(),
                    item,
                    fragment,
                    result
                )
            }
        }
    }
}

private fun openAlbum(fragment: Fragment) {
    val intent = Intent()
    intent.action = Intent.ACTION_GET_CONTENT
    val mimeTypes = arrayOf("image/*")
    intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
    intent.type = "image/*"
    if (fragment.isAdded) {
        fragment.startActivityForResult(intent, REQUEST_CODE_SELECT_PIC)
    }
}

private fun openAlbum(fragment: Fragment, result: ActivityResultLauncher<Intent>) {
    val intent = Intent()
    intent.action = Intent.ACTION_GET_CONTENT
    val mimeTypes = arrayOf("image/*")
    intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
    intent.type = "image/*"
    if (fragment.isAdded) {
        result.launch(intent)
    }
}


private fun openCamera(fragment: Fragment) {
    Intent(MediaStore.ACTION_IMAGE_CAPTURE).let { intent ->
        fragment.context?.let { context ->
            if (context.packageManager != null) {
                val uri = getCameraOutputUri(context)
                if (uri != null) {
                    val resInfoList = context.packageManager.queryIntentActivities(
                        intent,
                        PackageManager.MATCH_DEFAULT_ONLY
                    )
                    resInfoList.forEach { resolveInfo ->
                        val name = resolveInfo.activityInfo.packageName
                        fragment.context?.grantUriPermission(
                            name,
                            uri,
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                        )
                    }
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
                    try {
                        if (fragment.isAdded) {
                            fragment.startActivityForResult(intent, REQUEST_CODE_SELECT_CAMERA)
                        }
                    } catch (e: SecurityException) {
                        AppLogger.e(TAG, "Failed to open camera", e)
                    }
                }
            }
        }
    }
}

private fun openCamera(fragment: Fragment, result: ActivityResultLauncher<Intent>) {
    Intent(MediaStore.ACTION_IMAGE_CAPTURE).let { intent ->
        fragment.context?.let { context ->
            if (context.packageManager != null) {
                val uri = getCameraOutputUri(context)
                if (uri != null) {
                    val resInfoList = context.packageManager.queryIntentActivities(
                        intent,
                        PackageManager.MATCH_DEFAULT_ONLY
                    )
                    resInfoList.forEach { resolveInfo ->
                        val name = resolveInfo.activityInfo.packageName
                        fragment.context?.grantUriPermission(
                            name,
                            uri,
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                        )
                    }
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
                    try {
                        if (fragment.isAdded) {
                            result.launch(intent)
                        }
                    } catch (e: SecurityException) {
                        AppLogger.e(TAG, "Failed to open camera", e)
                    }
                }
            }
        }
    }
}

private fun checkListOfPermission(
    activity: Activity,
    permissions: Collection<String>,
    item: CHOOSER,
    fragment: Fragment,
    result: ActivityResultLauncher<Intent>? = null
) {
    Dexter.withContext(activity)
        .withPermissions(permissions)
        .withListener(object : MultiplePermissionsListener {

            override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {

                p0?.let {
                    if (it.areAllPermissionsGranted()) {
                        if (item == CHOOSER.CAMERA) {
                            when (result) {
                                null -> openCamera(fragment)
                                else -> openCamera(fragment, result)
                            }
                        } else if (item == CHOOSER.GALLERY) {
                            when (result) {
                                null -> openAlbum(fragment)
                                else -> openAlbum(fragment, result)
                            }
                        }
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


fun setImage(imageView: ImageView, url: String?, drawable: Int, circle: Boolean = false) {
    try {
        Glide.with(imageView.context).clear(imageView)
        if (url.isNullOrBlank().not()) {
            if (circle) {
                Glide.with(imageView.context)
                    .asBitmap()
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(drawable)
                    .placeholder(drawable)
                    .into(object : BitmapImageViewTarget(imageView) {
                        override fun setResource(resource: Bitmap?) {
                            val circularBitmapDrawable: RoundedBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(
                                    BaseApplication.instance.resources,
                                    resource
                                )
                            circularBitmapDrawable.setAntiAlias(true)
                            circularBitmapDrawable.isCircular = true
                            imageView.setImageDrawable(circularBitmapDrawable)
                        }
                    })
            } else {
                Glide.with(imageView.context)
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(drawable)
                    .placeholder(drawable)
                    .into(imageView)
            }
        } else {
            imageView.setImageResource(drawable)
        }
    } catch (e: IllegalArgumentException) {
        AppLogger.e(TAG, "setImage IllegalArgumentException ${e.localizedMessage}")
    }


}

fun getCameraOutputUri(context: Context): Uri? {
    val file = File(
        BaseApplication.instance.applicationContext.externalCacheDir,
        "Editor"
    )
    var uri: Uri? = null
    try {
        uri = FileProvider.getUriForFile(context, context.packageName + ".files", file)
    } catch (e: IllegalArgumentException) {
        AppLogger.e("ProfileSet", "IllegalArgumentException", e)
    }
    return uri
}

 fun saveImageToGallery(imagePath: String, context: Context,callback:(Boolean)->Unit) {
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, File(imagePath).name)
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
    }

    val resolver = context.contentResolver
    var uri: Uri? = null

    try {
        uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        if (uri != null) {
            resolver.openOutputStream(uri)?.use { outputStream ->
                File(imagePath).inputStream().use { inputStream ->
                    inputStream.copyTo(outputStream)
                }
            }

            // Notify the system that a new image has been added to the gallery
            Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri).also { scanIntent ->
                context.sendBroadcast(scanIntent)
            }

            callback.invoke(true)
        } else {
            callback.invoke(false)
        }
    } catch (e: Exception) {
        callback.invoke(false)
     }
}

