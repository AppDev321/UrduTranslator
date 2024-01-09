package com.core.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.provider.Settings
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import androidx.core.app.ActivityCompat
import com.core.BaseApplication
import com.core.extensions.TAG
import com.core.extensions.empty
import com.core.utils.Utils.androidNougatAndAbove
import com.core.utils.Utils.androidQAndAbove


object NetworkStatusListener {

    private var callbackList: MutableList<INetworkCallback> = arrayListOf()

    fun registerNetworkCallback(callback: INetworkCallback) {
        callbackList.add(callback)
    }

    fun updateCallback(isNetworkAvailable : Boolean){
        callbackList.forEach {
            if(isNetworkAvailable) {
                it.onNetworkAvailable()
            }else {
                it.onNetworkUnAvailable()
            }
        }
    }

    fun unRegisterNetworkCallback(callback: INetworkCallback) {
        callbackList.remove(callback)
    }

    interface INetworkCallback {

        fun onNetworkAvailable()

        fun onNetworkUnAvailable()

    }

    fun hasInternetConnection(): Boolean {
        val cm =
            BaseApplication.instance.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        return try {
            val activeNetwork =
                cm?.activeNetworkInfo
            activeNetwork != null && (activeNetwork.isConnected || activeNetwork.type == ConnectivityManager.TYPE_ETHERNET)
        } catch (e: RuntimeException) {
            AppLogger.d(TAG, "unable to check for internet connection", e)
            true //if internet connection can not be checked it is probably best to just try
        }
    }

    fun getActiveNetworkType(): String {
        val connectivityManager =
            BaseApplication.instance.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        try {
            val capabilities: NetworkCapabilities? =
                connectivityManager?.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return NetworkStatus.WIFI.name
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        if (isDataRoaming()) {
                            return NetworkStatus.ROAMING.name
                        }
                        return NetworkStatus.CELLULAR.name
                    }
                }
            }
        } catch (e: RuntimeException) {
            AppLogger.e(TAG, "unable to check for connected network status", e)
        }
        return String.empty
    }

    private fun isDataRoaming(): Boolean {
        try {
            if (androidQAndAbove) {
                val telephonyManager: TelephonyManager =
                    BaseApplication.instance.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                return telephonyManager.isNetworkRoaming
            }
            if (androidNougatAndAbove) {
                val subscriptionManager: SubscriptionManager =
                    BaseApplication.instance.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager
                val id = SubscriptionManager.getDefaultDataSubscriptionId()
                if (ActivityCompat.checkSelfPermission(
                        BaseApplication.instance,
                        Manifest.permission.READ_PHONE_STATE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
                val info = subscriptionManager.getActiveSubscriptionInfo(id)
                if(info != null) {
                    return subscriptionManager.isNetworkRoaming(info.subscriptionId)
                }
            }
        } catch (exception: Settings.SettingNotFoundException) {
            AppLogger.e(TAG, "data roaming settings not available in device")
        }
        return false
    }

    enum class NetworkStatus {
        CELLULAR,
        WIFI,
        ROAMING
    }
}