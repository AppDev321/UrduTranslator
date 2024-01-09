package com.core.service;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;


import com.core.BaseApplication;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class ConnectionWorker extends Worker {
    public static final String TAG = ConnectionWorker.class.getSimpleName();

    public static final String NAME_PERIODIC = "ConnectionWorker-Periodic";
    public static final String NAME_ONETIME = "ConnectionWorker-OneTime";

    public ConnectionWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NotNull
    @Override
    public Result doWork() {

       Log.e(TAG, "doWork " + getId() + " on " + Thread.currentThread());
        tryToStartService(BaseApplication.instance);

        return  Result.success();
    }

    private void tryToStartService(Context context) {
        Intent intent = new Intent(context, BackgroundService.class);
         PendingIntent pendingIntent = PendingIntent.getService(context, 101, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            final long timeToWake = SystemClock.elapsedRealtime() + 2 * 1000;
            if (alarmManager != null) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, timeToWake, pendingIntent);
            }
    }
}
