package au.edu.swin.sdmd.smarthome.alarms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import au.edu.swin.sdmd.smarthome.data.SmartHomeDatabase
import au.edu.swin.sdmd.smarthome.data.air_conditioner_trigger.AirConditionerAction
import au.edu.swin.sdmd.smarthome.data.air_conditioner_trigger.AirConditionerTrigger
import au.edu.swin.sdmd.smarthome.notifications.TriggerNotificationService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class AirConditionerTriggerReceiver: BroadcastReceiver() {
    val scope = CoroutineScope(SupervisorJob())

    override fun onReceive(context: Context, intent: Intent) {
        val airConditionerTrigger = intent.extras?.getParcelable<AirConditionerTrigger>("trigger") ?: return

        val airConditionerDao = SmartHomeDatabase.getDatabase(context).airConditionerDao()
        val airConditionerTriggerScheduler = AirConditionerTriggerScheduler(context)
        val triggerNotificationService = TriggerNotificationService(context)

        val pendingResult: PendingResult = goAsync()

        Log.d("AC_TRIGGER_RECEIVER", airConditionerTrigger.action.value)

        scope.launch(Dispatchers.Default) {
            try {
                var notificationMessage = ""

                when (airConditionerTrigger.action) {
                    AirConditionerAction.TURN_ON -> {
                        airConditionerDao.turnOn(airConditionerTrigger.airConditionerId)
                        notificationMessage = "Turned on air conditioner"
                        Log.d("AC_TRIGGER_RECEIVER", "Turning on")
                    }
                    AirConditionerAction.TURN_OFF -> {
                        airConditionerDao.turnOff(airConditionerTrigger.airConditionerId)
                        notificationMessage = "Turned off air conditioner"
                        Log.d("AC_TRIGGER_RECEIVER", "Turning off")
                    }
                }

                airConditionerTriggerScheduler.schedule(airConditionerTrigger)
                triggerNotificationService.showNotification(notificationMessage)
            } finally {
                pendingResult.finish()
            }
        }
    }
}