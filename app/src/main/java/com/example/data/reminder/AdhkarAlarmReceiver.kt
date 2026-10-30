package com.example.data.reminder

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.MainActivity

class AdhkarAlarmReceiver : BroadcastReceiver() {

    companion object {
        const val CHANNEL_ID = "zikr_reminders_channel"
        const val EXTRA_TYPE = "extra_reminder_type"
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_NOTE = "extra_note"
        const val EXTRA_NOTIFICATION_ID = "extra_notification_id"

        const val TYPE_MORNING = "MORNING_ADHKAR"
        const val TYPE_EVENING = "EVENING_ADHKAR"
        const val TYPE_PRAYER = "PRAYER"
        const val TYPE_CALENDAR_EVENT = "CALENDAR_EVENT"
        const val TYPE_ISLAMIC_HOLIDAY = "ISLAMIC_HOLIDAY"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val type = intent.getStringExtra(EXTRA_TYPE) ?: TYPE_MORNING
        val customTitle = intent.getStringExtra(EXTRA_TITLE)
        val customNote = intent.getStringExtra(EXTRA_NOTE)
        val customNotifId = intent.getIntExtra(EXTRA_NOTIFICATION_ID, 2000)

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "ZIKR LIFE Worship & Calendar Reminders",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Remembrances, prayer times, Islamic special days, and personal calendar goals"
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(channel)
        }

        val launchIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            customNotifId,
            launchIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val (title, content) = when (type) {
            TYPE_MORNING -> "Morning Adhkar • ZIKR LIFE" to "Begin your day in the protection of Allah. Read your morning remembrances."
            TYPE_EVENING -> "Evening Adhkar • ZIKR LIFE" to "As the sun sets, guard your heart with the evening adhkar and du'as."
            TYPE_CALENDAR_EVENT -> (customTitle ?: "Calendar Milestone • ZIKR LIFE") to (customNote?.takeIf { it.isNotBlank() } ?: "Scheduled Islamic calendar reminder.")
            TYPE_ISLAMIC_HOLIDAY -> (customTitle ?: "Blessed Islamic Day • ZIKR LIFE") to (customNote ?: "Special day of worship and remembrance on the Hijri calendar.")
            else -> "Time to Remember Allah" to "Pause your day and connect with your Creator."
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle(title)
            .setContentText(content)
            .setStyle(NotificationCompat.BigTextStyle().bigText(content))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(customNotifId, notification)

        // Reschedule daily alarms
        if (type == TYPE_MORNING || type == TYPE_EVENING) {
            ReminderScheduler.rescheduleNextDay(context, type)
        }
    }
}
