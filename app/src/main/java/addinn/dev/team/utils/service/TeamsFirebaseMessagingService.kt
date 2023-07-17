package addinn.dev.team.utils.service

import addinn.dev.team.R
import addinn.dev.team.SplashScreen
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class TeamsFirebaseMessagingService : FirebaseMessagingService() {
    private val CHANNEL_ID = "Team_Channel"

    override fun onNewToken(token: String) {
        println("NEW_TOKEN: $token")
        super.onNewToken(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        if (isAppInBackground()) {
            // DATA
            val message = remoteMessage.data["message"]
            val fromUserId = remoteMessage.data["fromId"]

            // NOTIFICATION
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notificationId = fromUserId.hashCode()

            createNotificationChannel(notificationManager)

            val intent = Intent(this, SplashScreen::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            val pendingIntent = PendingIntent.getActivity(
                this, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )

            val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(fromUserId)
                .setContentText(message)
                .setSmallIcon(R.drawable.addinn_logo)
                .setAutoCancel(true)
                .setSubText("Teams ADDINN")
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent)
                .build()

            notificationManager.notify(notificationId, notification)
        }
    }

    private fun createNotificationChannel(notificationManager: NotificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Teams ADDINN"
            val descriptionText = "New message from Teams ADDINN"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun isAppInBackground(): Boolean {
        return ProcessLifecycleOwner.get().lifecycle.currentState == Lifecycle.State.CREATED
    }
}
