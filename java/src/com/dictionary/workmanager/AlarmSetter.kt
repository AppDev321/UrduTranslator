package com.dictionary.workmanager

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent

object AlarmSetter {

    fun setAlarmWithInterval(context: Context, interval: Long, alarmTime: Long) {
        val alarmIntent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context, 1002,
            alarmIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val manager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        manager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            alarmTime,
            interval,
            pendingIntent
        )
    }

    enum class BackupAlarmInterval(var interval: Long) {
        EVENING(61200000), //5PM
        NIGHT(72000000),
        MORNING(28800000);

        companion object {
            fun getOrdinalByInterval(time: Long): Int? {
                return values().find { it.interval == time }?.ordinal
            }

            fun getInternalByOrdinal(ordinal: Int): Long {
                return values()[ordinal].interval
            }

            fun getBackupAlarmIntervalByOrdinal(ordinal: Int): BackupAlarmInterval {
                return values()[ordinal]
            }

            fun getBackupAlarmIntervalByInterval(time: Long): BackupAlarmInterval? {
                return values().find { it.interval == time }
            }
        }
    }
}