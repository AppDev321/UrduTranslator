package com.core.utils

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings
import com.core.extensions.empty
import com.core.extensions.safeGet
import javax.inject.Inject

class DeviceInfoUtilClass @Inject constructor(private val context: Context) {
    val model: String = Build.MODEL
    val brand: String = Build.BRAND
    val osVersion = Build.VERSION.SDK_INT.toString()

    private var androidId: String = String.empty

    @SuppressLint("HardwareIds")
    fun getAndroidId(): String {
        if (androidId.isEmpty())
            androidId =
                Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
                    .safeGet()
        return androidId
    }

}