package com.vipet.petvip.Service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.vipet.petvip.Account.LoginActivity
import com.vipet.petvip.R

class FcmService : FirebaseMessagingService() {

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        Log.e("message", p0.data["body"])
        sendNotification(p0.data["body"])
    }

    private fun sendNotification(body: String?) {
        val intent = Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("Notification", body)
        }

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        val notificationBuilder = NotificationCompat.Builder(this)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("예약 알림")
            .setContentText(body)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)


        val notificationManager: NotificationManager =
            this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        getChannel()?.let { channel ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationManager.createNotificationChannel(channel)
                notificationBuilder.setChannelId("channel_pet.vip")
            }
        }
        notificationManager.notify(0, notificationBuilder.build())
    }

    private fun getChannel(): NotificationChannel? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                "channel_pet.vip",
                "channel_pet_vip",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationChannel.description = "예약정보 알림 채널"
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            val notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            notificationChannel.setSound(notificationSound, null)
            notificationChannel.enableVibration(true)
            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            notificationChannel
        } else {
            null
        }
    }

}