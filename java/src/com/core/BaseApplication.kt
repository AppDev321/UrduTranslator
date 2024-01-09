package com.core

import android.app.Application
import com.core.utils.AppLogger
import com.core.utils.PreferenceManager
import com.core.utils.fileUtils.FileUtils
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject


@HiltAndroidApp
class BaseApplication : Application() /*, androidx.lifecycle.DefaultLifecycleObserver */ {
    companion object {
        lateinit var instance: BaseApplication
    }

    @Inject
    lateinit var preferenceManager: PreferenceManager
    override fun onCreate() {
        super<Application>.onCreate()

        instance = this
        AppLogger.initializeLogging(
            this, FileUtils.getLogsPath(), preferenceManager.isApplicationLogsEnabled()
        )


        /*  ProcessLifecycleOwner.get().lifecycle.addObserver(this)
          CoroutineScope(Dispatchers.IO).launch {
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

        override fun onStop(owner: LifecycleOwner) {
            super.onStop(owner)
            startService()
            AppLogger.e(TAG, "************* backgrounded")
        }*/

}