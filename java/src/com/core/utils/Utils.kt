package com.core.utils

import android.Manifest
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.media.ExifInterface
import android.os.Build
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.text.Html
import android.text.Spanned
import android.text.TextUtils
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.getSystemService
import androidx.core.text.HtmlCompat
import com.core.extensions.empty
import com.core.extensions.safeGet
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.roundToInt


object Utils {

    val androidMarshMellow: Boolean
        get() = Build.VERSION.SDK_INT == Build.VERSION_CODES.M

    val androidNougatAndAbove: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N

    val androidOreoAndAbove: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O

    val androidPieAndAbove: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.P

    val androidQAndAbove: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

    val androidRAndAbove: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.R


    // TODO - Since target SDK is 30 , version code for S is not available , to be refactored
    // when target SDK updated to 31
    val androidSAndAbove: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

    val androidTAndAbove: Boolean
        get() = Build.VERSION.SDK_INT > Build.VERSION_CODES.S_V2

    val androidTIRAMISUAndAbove: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

    val androidUpsideDownCakeAndAbove: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE


    fun compressImage(filePath: String?): File? {
        try {
            var scaledBitmap: Bitmap? = null
            val options =
                BitmapFactory.Options()
            options.inJustDecodeBounds = true
            var bmp =
                BitmapFactory.decodeFile(filePath, options)
            var actualHeight = options.outHeight
            var actualWidth = options.outWidth
            val maxHeight = 816.0f
            val maxWidth = 612.0f
            var imgRatio = actualWidth / actualHeight.toFloat()
            val maxRatio = maxWidth / maxHeight
            if (actualHeight > maxHeight || actualWidth > maxWidth) {
                if (imgRatio < maxRatio) {
                    imgRatio = maxHeight / actualHeight
                    actualWidth = (imgRatio * actualWidth).toInt()
                    actualHeight = maxHeight.toInt()
                } else if (imgRatio > maxRatio) {
                    imgRatio = maxWidth / actualWidth
                    actualHeight = (imgRatio * actualHeight).toInt()
                    actualWidth = maxWidth.toInt()
                } else {
                    actualHeight = maxHeight.toInt()
                    actualWidth = maxWidth.toInt()
                }
            }
            options.inSampleSize =
                calculateInSampleSize(options, actualWidth, actualHeight)
            options.inJustDecodeBounds = false
            options.inDither = false
            options.inPurgeable = true
            options.inInputShareable = true
            options.inTempStorage = ByteArray(2 * 1048576)
            try {
                bmp = BitmapFactory.decodeFile(filePath, options)
            } catch (exception: OutOfMemoryError) {
                exception.printStackTrace()
            }
            try {
                scaledBitmap = Bitmap.createBitmap(
                    actualWidth,
                    actualHeight,
                    Bitmap.Config.ARGB_8888
                )
            } catch (exception: OutOfMemoryError) {
                exception.printStackTrace()
            }
            val ratioX = actualWidth / options.outWidth.toFloat()
            val ratioY = actualHeight / options.outHeight.toFloat()
            val middleX = actualWidth / 2.0f
            val middleY = actualHeight / 2.0f
            val scaleMatrix = Matrix()
            scaleMatrix.setScale(ratioX, ratioY, middleX, middleY)
            val canvas: Canvas
            if (scaledBitmap != null) {
                canvas = Canvas(scaledBitmap)
                canvas.setMatrix(scaleMatrix)
                canvas.drawBitmap(
                    bmp,
                    middleX - bmp.width / 2,
                    middleY - bmp.height / 2,
                    Paint(Paint.FILTER_BITMAP_FLAG)
                )
            }
            val exif: ExifInterface
            try {
                exif = ExifInterface(filePath.safeGet())
                val orientation =
                    exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0)
                val matrix = Matrix()
                if (orientation == 6) {
                    matrix.postRotate(90f)
                } else if (orientation == 3) {
                    matrix.postRotate(180f)
                } else if (orientation == 8) {
                    matrix.postRotate(270f)
                }
                if (scaledBitmap != null) {
                    scaledBitmap = Bitmap.createBitmap(
                        scaledBitmap,
                        0,
                        0,
                        scaledBitmap.width,
                        scaledBitmap.height,
                        matrix,
                        true
                    )
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            val out: FileOutputStream
            try {
                out = FileOutputStream(filePath)
                scaledBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, out)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return File(filePath)
    }

    fun compressImageForThumb(filePath: String?): File? {
        try {
            var scaledBitmap: Bitmap? = null
            val options =
                BitmapFactory.Options()
            options.inJustDecodeBounds = true
            var bmp =
                BitmapFactory.decodeFile(filePath, options)
            var actualHeight = options.outHeight
            var actualWidth = options.outWidth
            val maxHeight = 120.0f
            val maxWidth = 120.0f
            var imgRatio = actualWidth / actualHeight.toFloat()
            val maxRatio = maxWidth / maxHeight
            if (actualHeight > maxHeight || actualWidth > maxWidth) {
                if (imgRatio < maxRatio) {
                    imgRatio = maxHeight / actualHeight
                    actualWidth = (imgRatio * actualWidth).toInt()
                    actualHeight = maxHeight.toInt()
                } else if (imgRatio > maxRatio) {
                    imgRatio = maxWidth / actualWidth
                    actualHeight = (imgRatio * actualHeight).toInt()
                    actualWidth = maxWidth.toInt()
                } else {
                    actualHeight = maxHeight.toInt()
                    actualWidth = maxWidth.toInt()
                }
            }
            options.inSampleSize =
                calculateInSampleSize(options, actualWidth, actualHeight)
            options.inJustDecodeBounds = false
            options.inDither = false
            options.inPurgeable = true
            options.inInputShareable = true
            options.inTempStorage = ByteArray(2 * 256000)
            try {
                bmp = BitmapFactory.decodeFile(filePath, options)
            } catch (exception: OutOfMemoryError) {
                exception.printStackTrace()
            }
            try {
                scaledBitmap = Bitmap.createBitmap(
                    actualWidth,
                    actualHeight,
                    Bitmap.Config.ARGB_8888
                )
            } catch (exception: OutOfMemoryError) {
                exception.printStackTrace()
            }
            val ratioX = actualWidth / options.outWidth.toFloat()
            val ratioY = actualHeight / options.outHeight.toFloat()
            val middleX = actualWidth / 2.0f
            val middleY = actualHeight / 2.0f
            val scaleMatrix = Matrix()
            scaleMatrix.setScale(ratioX, ratioY, middleX, middleY)
            val canvas: Canvas
            if (scaledBitmap != null) {
                canvas = Canvas(scaledBitmap)
                canvas.setMatrix(scaleMatrix)
                canvas.drawBitmap(
                    bmp,
                    middleX - bmp.width / 2,
                    middleY - bmp.height / 2,
                    Paint(Paint.FILTER_BITMAP_FLAG)
                )
            }
            val exif: ExifInterface
            try {
                exif = ExifInterface(filePath.safeGet())
                val orientation =
                    exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0)
                val matrix = Matrix()
                if (orientation == 6) {
                    matrix.postRotate(90f)
                } else if (orientation == 3) {
                    matrix.postRotate(180f)
                } else if (orientation == 8) {
                    matrix.postRotate(270f)
                }
                if (scaledBitmap != null) {
                    scaledBitmap = Bitmap.createBitmap(
                        scaledBitmap,
                        0,
                        0,
                        scaledBitmap.width,
                        scaledBitmap.height,
                        matrix,
                        true
                    )
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            val out: FileOutputStream
            try {
                out = FileOutputStream(filePath)
                scaledBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, out)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return File(filePath)
    }

    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int
    ): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {
            val heightRatio =
                Math.round(height.toFloat() / reqHeight.toFloat())
            val widthRatio =
                Math.round(width.toFloat() / reqWidth.toFloat())
            inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
        }
        val totalPixels = width * height.toFloat()
        val totalReqPixelsCap = reqWidth * reqHeight * 2.toFloat()
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++
        }
        return inSampleSize
    }

    /**
     * @return The MIME type for the given file.
     */
    fun getMimeType(file: File): String? {
        val extension = getExtension(file.name)
        if (!TextUtils.isEmpty(extension)) {
            val mimeTypeFromExtension = MimeTypeMap.getSingleton()
                .getMimeTypeFromExtension(extension.substring(1).toLowerCase())
            return if (TextUtils.isEmpty(mimeTypeFromExtension)) com.core.utils.MimeTypeUtil.getType(
                file.name
            ) else mimeTypeFromExtension
        }
        return "application/octet-stream"
    }

    /**
     * Gets the extension of a file name, like ".png" or ".jpg".
     *
     * @param uri
     * @return Extension including the dot("."); "" if there is no extension;
     * null if uri was null.
     */
    fun getExtension(uri: String?): String {
        if (uri == null) {
            return String.empty
        }
        val dot = uri.lastIndexOf(".")
        return if (dot >= 0) {
            var substring = uri.substring(dot)
            if (substring.contains("?")) {
                substring = substring.substring(0, substring.indexOf("?"))
            }
            substring
        } else {
            // No extension.
            String.empty
        }
    }

    fun getMilliFromDate(dateToFormat: String?): Long {
        return if (dateToFormat?.isEmpty() == true) {
            0
        } else {
            var date = Date()
            val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ROOT)
            formatter.timeZone = TimeZone.getTimeZone("GMT");
            try {
                date = formatter.parse(dateToFormat)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            date.time
        }
    }

    private const val BITMAP_SCALE = 0.5f
    private const val BLUR_RADIUS = 25f

    fun blur(context: Context?, image: Bitmap?): Bitmap? {
        if (image == null)
            return null
        val width = (image.width * BITMAP_SCALE).roundToInt()
        val height = (image.height * BITMAP_SCALE).roundToInt()
        val inputBitmap = Bitmap.createScaledBitmap(image, width, height, false)
        val outputBitmap = Bitmap.createBitmap(inputBitmap)
        val rs = RenderScript.create(context)
        val intrinsicBlur =
            ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
        val tmpIn = Allocation.createFromBitmap(rs, inputBitmap)
        val tmpOut = Allocation.createFromBitmap(rs, outputBitmap)
        intrinsicBlur.setRadius(BLUR_RADIUS)
        intrinsicBlur.setInput(tmpIn)
        intrinsicBlur.forEach(tmpOut)
        tmpOut.copyTo(outputBitmap)
        return outputBitmap
    }

    fun isSkip(permission: String, config: Boolean): Boolean {
        if (androidQAndAbove &&
            config && permission == Manifest.permission.READ_EXTERNAL_STORAGE
        ) {
            return true
        } else if (androidQAndAbove.not() && config && permission == Manifest.permission.WRITE_EXTERNAL_STORAGE) {
            return true
        }
        return false
    }

    fun getHtmlSpannedText(htmlString: String): Spanned {
        return if (androidNougatAndAbove) {
            Html.fromHtml(htmlString, HtmlCompat.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(htmlString)
        }
    }

     fun isBluetoothSupported(context: Context) :Boolean {
         return context.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)
    }

     fun isBluetoothEnabled(context:Context): Boolean {
        val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val adapter = bluetoothManager.adapter
        return adapter != null && adapter.isEnabled
    }


}