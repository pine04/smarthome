package au.edu.swin.sdmd.smarthome.alarms

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import au.edu.swin.sdmd.smarthome.data.light_trigger.LightTrigger
import java.time.OffsetDateTime

class LightTriggerScheduler(
    private val context: Context
) {
    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    fun schedule(lightTrigger: LightTrigger) {
        val intent = Intent(context, LightTriggerReceiver::class.java).apply {
            putExtra("trigger", lightTrigger)
        }

        val triggerTimeToday = OffsetDateTime.now()
            .withHour(lightTrigger.time.hour)
            .withMinute(lightTrigger.time.minute)
            .withSecond(0)

        val triggerTime = if (OffsetDateTime.now().toEpochSecond() - triggerTimeToday.toEpochSecond() >= 0) {
            triggerTimeToday.plusDays(1)
        } else {
            triggerTimeToday
        }

        Log.d("LIGHT_TRIGGER_SCHEDULE", "Trigger scheduled to fire at $triggerTime.")

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerTime.toEpochSecond() * 1000,
            PendingIntent.getBroadcast(
                context,
                lightTrigger.triggerId.toInt(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    fun cancel(lightTrigger: LightTrigger) {
        Log.d("LIGHT_TRIGGER_CANCEL", "Trigger cancelled.")

        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                lightTrigger.triggerId.toInt(),
                Intent(context, LightTriggerReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}