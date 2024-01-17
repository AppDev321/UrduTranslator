package com.core

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import com.core.extensions.TAG
import com.core.utils.AppLogger
import com.core.utils.PreferenceManager
import com.core.utils.fileUtils.FileUtils
import com.dictionary.workmanager.AlarmSetter
import com.dictionary.workmanager.NotificationReceiver
import com.dictionary.workmanager.QuizNotificationWorker
import com.dictionary.workmanager.WordNotificationWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@HiltAndroidApp
class BaseApplication : Application() , androidx.lifecycle.DefaultLifecycleObserver  {
    companion object {
        lateinit var instance: BaseApplication
    }

    @Inject
    lateinit var preferenceManager: PreferenceManager
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super<Application>.onCreate()

        instance = this
        AppLogger.initializeLogging(
            this, FileUtils.getLogsPath(), preferenceManager.isApplicationLogsEnabled()
        )

        createQuizWorkerService()
        createWordWorkerService()

        val selectedInterval = AlarmSetter.BackupAlarmInterval.getInternalByOrdinal(0)
        val nextBackupTime = AlarmSetter.BackupAlarmInterval.EVENING.interval
        AlarmSetter.setAlarmWithInterval(
           this,
            selectedInterval, nextBackupTime
        )

       /* val alarmScheduler: AlarmScheduler = AlarmSchedulerImpl(this)
        val alarmItem=  AlarmItem(
            message = "Message",
            alarmHour =  20,
            alarmMin = 0
        )
        alarmItem.let(alarmScheduler::schedule)
        val alarmScheduler2: AlarmScheduler = AlarmSchedulerImpl(this)
        val alarmItem2=  AlarmItem(
            message = "Message",
            alarmHour =  17,
            alarmMin = 0
        )
        alarmItem2.let(alarmScheduler2::schedule)

*/
      /*  createWorkerService()
        createDailyQuizNotificationAlarm()*/
          ProcessLifecycleOwner.get().lifecycle.addObserver(this)
         /* CoroutineScope(Dispatchers.IO).launch {
              AppInitializer.getInstance(this@BaseApplication).apply {
                  initializeComponent(FirebaseStartup::class.java)
              }
          }
         startService()*/


    }

    /*
        private fun startService() {
            val intent = Intent(this, BackgroundService::class.java)
            try {
                startService(intent)
            } catch (e: IllegalStateException) {
                Log.e(
                    javaClass.simpleName,
                    "unable to start service from " + javaClass.simpleName
                )
            }
        }

        fun enqueueOnetimeConnectionWorker() {
            val workManager = WorkManager.getInstance(this)
            workManager.pruneWork()
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED).build()
            val connectionWorker =
                OneTimeWorkRequest.Builder(ConnectionWorker::class.java).addTag(ConnectionWorker.TAG)
                    .setConstraints(constraints).build()
            AppLogger.e(TAG, "enqueue OneTime ConnectionWorker " + connectionWorker.id)
            workManager.enqueueUniqueWork(
                ConnectionWorker.NAME_ONETIME, ExistingWorkPolicy.REPLACE, connectionWorker
            )
        }

       */

     fun createWordWorkerService()
    {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .build()
        val notificationWorkRequest =
            PeriodicWorkRequestBuilder<WordNotificationWorker>(1, TimeUnit.DAYS)
                .setConstraints(constraints)
                .setInitialDelay(calculateInitialDelay(17,0), TimeUnit.MILLISECONDS)
                .build()

     //   WorkManager.getInstance(this).enqueue(notificationWorkRequest)

    }
    fun createQuizWorkerService()
    {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .build()
        val notificationWorkRequest =
            PeriodicWorkRequestBuilder<QuizNotificationWorker>(1, TimeUnit.DAYS)
                .setConstraints(constraints)
                .setInitialDelay(calculateInitialDelay(20,0), TimeUnit.MILLISECONDS)
                .build()

      //  WorkManager.getInstance(this).enqueue(notificationWorkRequest)

    }


    private fun calculateInitialDelay(hour:Int,minute:Int): Long {
        val now = Calendar.getInstance()
        val targetTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour) // 8 PM
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        return if(targetTime.before(now)){
            targetTime.add(Calendar.DATE, 1)
            targetTime.timeInMillis
        } else {
            targetTime.timeInMillis - now.timeInMillis
        }
    }


    private fun createDailyQuizNotificationAlarm()
    {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val notificationIntent = Intent(this, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            NotificationReceiver.NOTIFICATION1.toInt(),
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 17) // Set the hour to 5 PM
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 10)
        }

        // Set up repeating notification at 5 PM every day
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )

    }
    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        createQuizWorkerService()
        createWordWorkerService()
        AppLogger.e(TAG, "************* backgrounded")
    }



}