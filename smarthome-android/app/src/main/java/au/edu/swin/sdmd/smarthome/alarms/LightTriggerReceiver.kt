package au.edu.swin.sdmd.smarthome.alarms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import au.edu.swin.sdmd.smarthome.data.SmartHomeDatabase
import au.edu.swin.sdmd.smarthome.data.light_trigger.LightAction
import au.edu.swin.sdmd.smarthome.data.light_trigger.LightTrigger
import au.edu.swin.sdmd.smarthome.notifications.TriggerNotificationService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class LightTriggerReceiver: BroadcastReceiver() {
    val scope = CoroutineScope(SupervisorJob())

    override fun onReceive(context: Context, intent: Intent) {
        val lightTrigger = intent.extras?.getParcelable<LightTrigger>("trigger") ?: return

        val lightDao = SmartHomeDatabase.getDatabase(context).lightDao()
        val lightTriggerScheduler = LightTriggerScheduler(context)
        val triggerNotificationService = TriggerNotificationService(context)

        val pendingResult: PendingResult = goAsync()

        Log.d("LIGHT_TRIGGER_RECEIVER", lightTrigger.action.value)

        scope.launch(Dispatchers.Default) {
            try {
                var notificationMessage = ""

                when (lightTrigger.action) {
                    LightAction.TURN_ON -> {
                        lightDao.turnOn(lightTrigger.lightId)
                        notificationMessage = "Turned on light"
                        Log.d("LIGHT_TRIGGER_RECEIVER", "Turning on")
                    }
                    LightAction.TURN_OFF -> {
                        lightDao.turnOff(lightTrigger.lightId)
                        notificationMessage = "Turned off light"
                        Log.d("LIGHT_TRIGGER_RECEIVER", "Turning off")
                    }
                }

                lightTriggerScheduler.schedule(lightTrigger)
                triggerNotificationService.showNotification(notificationMessage)
            } finally {
                pendingResult.finish()
            }
        }
    }
}