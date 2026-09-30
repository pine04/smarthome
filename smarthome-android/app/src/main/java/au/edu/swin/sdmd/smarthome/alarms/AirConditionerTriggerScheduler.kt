package au.edu.swin.sdmd.smarthome.alarms

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import au.edu.swin.sdmd.smarthome.data.air_conditioner_trigger.AirConditionerTrigger
import au.edu.swin.sdmd.smarthome.data.light_trigger.LightTrigger
import java.time.OffsetDateTime

class AirConditionerTriggerScheduler(
    private val context: Context
) {
    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    fun schedule(airConditionerTrigger: AirConditionerTrigger) {
        val intent = Intent(context, AirConditionerTriggerReceiver::class.java).apply {
            putExtra("trigger", airConditionerTrigger)
        }

        val triggerTimeToday = OffsetDateTime.now()
            .withHour(airConditionerTrigger.time.hour)
            .withMinute(airConditionerTrigger.time.minute)
            .withSecond(0)

        val triggerTime = if (OffsetDateTime.now().toEpochSecond() - triggerTimeToday.toEpochSecond() >= 0) {
            triggerTimeToday.plusDays(1)
        } else {
            triggerTimeToday
        }

        Log.d("AC_TRIGGER_SCHEDULE", "Trigger scheduled to fire at $triggerTime.")

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerTime.toEpochSecond() * 1000,
            PendingIntent.getBroadcast(
                context,
                airConditionerTrigger.triggerId.toInt(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    fun cancel(airConditionerTrigger: AirConditionerTrigger) {
        Log.d("AC_TRIGGER_CANCEL", "Trigger cancelled.")

        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                airConditionerTrigger.triggerId.toInt(),
                Intent(context, AirConditionerTriggerScheduler::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}