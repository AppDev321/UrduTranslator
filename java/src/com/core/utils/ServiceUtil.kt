package com.core.utils

import android.app.ActivityManager
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import com.core.BaseApplication
import com.core.extensions.safeGet


// TODO - Convert to class and use using hilt inject
private const val TAG = "ServiceUtil"
fun isServiceRunning(serviceClass: Class<*>): Boolean {
    val activityManager =
        BaseApplication.instance.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val services = activityManager.getRunningServices(Int.MAX_VALUE)
    for (runningServiceInfo in services) {
        if (runningServiceInfo.service.className == serviceClass.name) {
            AppLogger.v(TAG, "${serviceClass.simpleName} Running ${true}")
            return true
        }
    }
    AppLogger.v(TAG, "${serviceClass.simpleName} Running ${false}")
    return false
}

fun stopForeGroundService() {
  /*  try {
        if (isServiceRunning(CallForeGroundService::class.java)) {
            val intent = Intent(BaseApplication.instance, CallForeGroundService::class.java)
            intent.action = CallForeGroundService.ACTION_STOP_FOREGROUND_SERVICE
            BaseApplication.instance.startService(intent)
        }
    } catch (e: Exception) {
        AppLogger.e(TAG, e.localizedMessage.safeGet())
    }*/
}

// TODO - Remove this method gradually
fun isApplicationInBackground(context: Context): Boolean {

    try {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningProcesses = am.runningAppProcesses ?: return false
        for (processInfo in runningProcesses) {
            if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                for (activeProcess in processInfo.pkgList) {
                    if (activeProcess == context.packageName) {
                        AppLogger.d(TAG, "isApplicationInBackground false")
                        return false
                    }
                }
            }
        }
    } catch (e: Exception) {
        AppLogger.e(TAG, e.message.safeGet())
    }
    return true
}


fun startServiceWithIntent(
    intent: Intent,
    recordLogsForFailedCases: Boolean = false,
    from: String? = null,
    successCallback: ((isServiceRunSuccessfully: Boolean) -> Unit)? = null
) {/*if (Utils.androidOreoAndAbove) {
        val action = intent.action ?: String.empty
        try {
            when (action) {
                ACTION_MESSAGE_NOTIFICATION_PUSH,
                ACTION_CALL_NOTIFICATION_PUSH -> {
                    BaseApplication.instance.startForegroundService(intent)
                    successCallback?.invoke(true)
                }

                else -> {
                    BaseApplication.instance.startService(intent)
                    successCallback?.invoke(true)
                }
            }
        } catch (exp: IllegalStateException) {
            if (action == ACTION_MESSAGE_NOTIFICATION_PUSH && recordLogsForFailedCases) {
                val type =
                    if (androidUpsideDownCakeAndAbove && exp is MissingForegroundServiceTypeException) {
                        exp.TAG
                    } else if (Utils.androidSAndAbove) {
                        when (exp) {
                            is ForegroundServiceStartNotAllowedException -> exp.TAG
                            is BackgroundServiceStartNotAllowedException -> exp.TAG
                            else -> "IllegalStateException"
                        }
                    } else "IllegalStateException"
            }
            schedulePendingIntentForIntent(intent)
        }
    } else {
        schedulePendingIntentForIntent(intent)
    }*/
}

fun schedulePendingIntentForIntent(intent: Intent) {
    try {
        val pendingIntent: PendingIntent = PendingIntent.getService(
            BaseApplication.instance,
            101,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val timeToWake: Long = SystemClock.elapsedRealtime() + 1000
        (BaseApplication.instance.getSystemService(Context.ALARM_SERVICE) as AlarmManager?)?.setExactAndAllowWhileIdle(
            AlarmManager.ELAPSED_REALTIME_WAKEUP, timeToWake, pendingIntent
        )
    } catch (e: SecurityException) {
        AppLogger.e(TAG, "SecurityException " + e.localizedMessage)
    } catch (e: RuntimeException) {
        AppLogger.d(TAG, "RuntimeException " + e.localizedMessage)
    }
}



