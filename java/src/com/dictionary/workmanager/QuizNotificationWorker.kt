package com.dictionary.workmanager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.android.inputmethod.latin.R
import com.core.BaseApplication
import com.core.extensions.TAG
import com.core.utils.AppLogger
import com.core.utils.Utils.androidOreoAndAbove

class QuizNotificationWorker(
    val appContext: Context,
    workerParams: WorkerParameters
) : Worker(appContext, workerParams) {

    companion object{
        const val CHANNEL_ID = "12312"
        const val NOTIFICATION_ID  = "23411"

    }



    override fun doWork(): Result {
        AppLogger.e(TAG,"Quiz Notification trigger")
        showNotification()
        appContext.let {
            it as BaseApplication
            it.createQuizWorkerService()
        }

        return Result.success()
    }

    private fun showNotification() {
        // Create a notification channel (for Android O and above)
        createNotificationChannel(appContext)

        // Build the notification
        val builder = NotificationCompat.Builder(appContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.splash_icon)
            .setContentTitle(appContext.resources.getString(R.string.dic_app_name))
            .setContentText("Check quiz of the day")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        // Show the notification
        with(NotificationManagerCompat.from(appContext)) {
            notify(NOTIFICATION_ID.toInt(), builder.build())
        }
    }

    private fun createNotificationChannel(context: Context?) {
        if (androidOreoAndAbove) {
            val name = "Dictionary"
            val descriptionText = "Dictionary app"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager =
                context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}