package com.dictionary.workmanager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.android.inputmethod.latin.R
import com.core.utils.PreferenceManager
import com.core.utils.Utils
import com.dictionary.activity.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var preferenceManager: PreferenceManager

    override fun onReceive(context: Context, intent: Intent) {
        context.apply {
            showNotification(this)
        }
    }


    private fun showNotification(appContext: Context) {
        // Create a notification channel (for Android O and above)
        createNotificationChannel(appContext)


        val intent = Intent(appContext, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("isNotification",true)

        val pendingIntent = PendingIntent.getActivity(
            appContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )


        // Build the notification
        val builder = NotificationCompat.Builder(appContext, NotificationReceiver.CHANNELID)
            .setSmallIcon(R.drawable.splash_icon)
            .setContentTitle(appContext.resources.getString(R.string.dic_app_name))
            .setContentText("Check your daily quiz")
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        // Show the notification
        with(NotificationManagerCompat.from(appContext)) {
            notify(NotificationReceiver.NOTIFICATION1.toInt(), builder.build())
        }
    }

    private fun createNotificationChannel(context: Context?) {
        if (Utils.androidOreoAndAbove) {
            val name = "Quiz Channel"
            val descriptionText = "Quiz app"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(NotificationReceiver.CHANNELID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager =
                context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}