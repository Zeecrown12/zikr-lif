package com.example.data.reminder

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import java.util.Calendar

object ReminderScheduler {

    const val REQUEST_CODE_MORNING = 101
    const val REQUEST_CODE_EVENING = 102

    fun scheduleMorningReminder(context: Context, hour: Int = 6, minute: Int = 30) {
        scheduleDailyAlarm(context, hour, minute, AdhkarAlarmReceiver.TYPE_MORNING, REQUEST_CODE_MORNING)
    }

    fun scheduleEveningReminder(context: Context, hour: Int = 17, minute: Int = 30) {
        scheduleDailyAlarm(context, hour, minute, AdhkarAlarmReceiver.TYPE_EVENING, REQUEST_CODE_EVENING)
    }

    fun cancelReminder(context: Context, requestCode: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        val intent = Intent(context, AdhkarAlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager?.cancel(pendingIntent)
    }

    fun rescheduleNextDay(context: Context, type: String) {
        if (type == AdhkarAlarmReceiver.TYPE_MORNING) {
            scheduleMorningReminder(context, 6, 30)
        } else if (type == AdhkarAlarmReceiver.TYPE_EVENING) {
            scheduleEveningReminder(context, 17, 30)
        }
    }

    private fun scheduleDailyAlarm(
        context: Context,
        hour: Int,
        minute: Int,
        type: String,
        requestCode: Int
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val intent = Intent(context, AdhkarAlarmReceiver::class.java).apply {
            putExtra(AdhkarAlarmReceiver.EXTRA_TYPE, type)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            } else {
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent
                )
            }
        } catch (_: SecurityException) {
            // If exact alarm permission is restricted, fallback to standard alarm
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }
    }
}
