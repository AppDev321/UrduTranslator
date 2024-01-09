package com.core.utils

import android.app.Activity
import android.graphics.Color
import android.view.View
import android.view.WindowManager
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat.setDecorFitsSystemWindows
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.core.utils.Utils.androidRAndAbove

object ThemeUtil {


    fun Activity.makeStatusBarTransparent(view: View) {
        window.apply {
            setDecorFitsSystemWindows(window, false)
            WindowInsetsControllerCompat(window, view).let { controller ->
                controller.systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_BARS_BY_SWIPE
            }
            statusBarColor = Color.TRANSPARENT
        }
    }

    fun Activity.applyStatusBarColor(view: View, @ColorRes color: Int) {
        window.apply {
            setDecorFitsSystemWindows(this, true)
            WindowInsetsControllerCompat(this, view).show(WindowInsetsCompat.Type.systemBars())
            statusBarColor = ContextCompat.getColor(this@applyStatusBarColor, color)
        }
    }


    fun Activity.makeStatusBarTransparent() {
        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            statusBarColor = Color.TRANSPARENT
        }
    }


    fun transparentStatusBar(activity: Activity) {
        activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        activity.window
            .addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        activity.window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
        activity.window.statusBarColor = Color.TRANSPARENT
        activity.window.navigationBarColor = Color.TRANSPARENT

        activity.window.navigationBarColor = Color.BLACK


        activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }

    fun addTransparencyOnStatusBar(activity: Activity) {
        activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        activity.window
            .addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        activity.window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_LAYOUT_STABLE)

    }
     fun setFullScreenWindow(activity: Activity) {
        if (androidRAndAbove) {
            activity.window.decorView.windowInsetsController?.hide(
                android.view.WindowInsets.Type.statusBars()
            )
        } else {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

}