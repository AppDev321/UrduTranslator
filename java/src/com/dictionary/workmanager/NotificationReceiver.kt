package com.dictionary.workmanager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.android.inputmethod.latin.R
import com.core.extensions.TAG
import com.core.utils.AppLogger
import com.core.utils.Utils


class NotificationReceiver : BroadcastReceiver() {

    companion object{
        val CHANNELID = "12312"
        val NOTIFICATION1 = "23411"
        val NOTIFICATION2 = "23411"
    }


    override fun onReceive(context: Context, intent: Intent?) {
        AppLogger.e(TAG,"Notificaiton recvier")
        showNotification(context)

        /*val alarmScheduler: AlarmScheduler = AlarmSchedulerImpl(context)
        val alarmItem=  AlarmItem(
            message = "Message",
            alarmHour =  20,
            alarmMin = 0
        )
        alarmItem.let(alarmScheduler::schedule)

        val alarmItem2=  AlarmItem(
            message = "Message",
            alarmHour =  17,
            alarmMin = 0
        )
        alarmItem2.let(alarmScheduler::schedule)*/
    }


    private fun showNotification(appContext: Context) {
        // Create a notification channel (for Android O and above)
        createNotificationChannel(appContext)

        // Build the notification
        val builder = NotificationCompat.Builder(appContext, CHANNELID)
            .setSmallIcon(R.drawable.splash_icon)
            .setContentTitle(appContext.resources.getString(R.string.dic_app_name))
            .setContentText("Check quiz of the day")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        // Show the notification
        with(NotificationManagerCompat.from(appContext)) {
            notify(NOTIFICATION1.toInt(), builder.build())
        }
    }

    private fun createNotificationChannel(context: Context?) {
        if (Utils.androidOreoAndAbove) {
            val name = "Quiz Channel"
            val descriptionText = "Quiz app"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNELID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager =
                context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
