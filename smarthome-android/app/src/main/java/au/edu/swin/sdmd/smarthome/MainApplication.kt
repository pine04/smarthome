package au.edu.swin.sdmd.smarthome

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import au.edu.swin.sdmd.smarthome.data.AppContainer
import au.edu.swin.sdmd.smarthome.notifications.TriggerNotificationService

class MainApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            TriggerNotificationService.TRIGGER_CHANNEL_ID,
            "Trigger",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.description = "Used to notify users when a trigger is activated."

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}