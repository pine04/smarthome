package au.edu.swin.sdmd.smarthome.notifications

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import au.edu.swin.sdmd.smarthome.MainActivity
import au.edu.swin.sdmd.smarthome.R

class TriggerNotificationService(
    private val context: Context
) {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun showNotification(message: String) {
        val activityPendingIntent = PendingIntent.getActivity(
            context,
            1,
            Intent(context, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, TRIGGER_CHANNEL_ID)
            .setSmallIcon(R.drawable.home_24px)
            .setContentTitle("Trigger activated")
            .setContentText(message)
            .setContentIntent(activityPendingIntent)
            .build()

        notificationManager.notify(1, notification)
    }

    companion object {
        const val TRIGGER_CHANNEL_ID = "trigger_channel"
    }
}