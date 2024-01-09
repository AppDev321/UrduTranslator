package com.core.utils

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.os.PowerManager
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.WindowManager
import com.core.BaseApplication
import com.core.extensions.TAG
import kotlin.math.roundToInt


fun getScreenHeight(): Int {
    val windowManager =
        BaseApplication.instance.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val realSize = Point()
    val realDisplay = windowManager.defaultDisplay
    realDisplay?.getRealSize(realSize)
    return realSize.y
}

fun getScreenWidth(): Int {
    val windowManager =
        BaseApplication.instance.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val realSize = Point()
    val realDisplay = windowManager.defaultDisplay
    realDisplay?.getRealSize(realSize)
    return realSize.x
}


fun getPixel(dp: Float): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp, BaseApplication.instance.resources.displayMetrics
    ).toInt()
}

private var density = 1f
fun dp(value: Float): Int {
    if (density == 1f) {
        checkDisplaySize()
    }
    return if (value == 0f) {
        0
    } else Math.ceil((density * value).toDouble()).toInt()
}

private fun checkDisplaySize() {
    try {
        density = BaseApplication.instance.resources.displayMetrics.density
    } catch (e: Exception) {
        e.printStackTrace()
    }

}

var chatBoxGap: Int = 0
fun setDeviceDisplayData(activity: Activity) {
    val displayMetrics = DisplayMetrics()
    activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
    chatBoxGap = ((displayMetrics.widthPixels / 100.0f) * 20).roundToInt()
}

fun getHalfScreenHeight(): Int {
    return (getScreenHeight() / 2)
}

fun getHalfScreenWidth(): Int {
    return (getScreenWidth() / 2)
}

fun getOneFourthScreenWidth(): Int {
    return (getScreenWidth() / 4)
}

fun Context.getActionBarSize(): Int {
    try {
        val tv = TypedValue()
        if (theme.resolveAttribute(androidx.appcompat.R.attr.actionBarSize, tv, true)) {
            return TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics)
        }
    } catch (e: Exception) {
        AppLogger.e(TAG, "actionBarSize " + e.localizedMessage)
    }
    return 0
}

fun getStatusBarHeight(context: Context?): Int {
    context?.run {
        val statusBarHeightId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (statusBarHeightId > 0)
            return resources.getDimensionPixelSize(statusBarHeightId)
    }
    return getPixel(24f)
}

fun isScreenOn(): Boolean {
    val powerManager =
        (BaseApplication.instance.getSystemService(Context.POWER_SERVICE)) as PowerManager
    return powerManager.isInteractive
}