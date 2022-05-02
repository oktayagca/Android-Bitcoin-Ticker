package com.example.bitcointicker.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.bitcointicker.R
import com.example.bitcointicker.ui.MainActivity

class NotificationHelper(private  val context: Context) {

    companion object{
        const val NOTIFICATION_ID ="1001"
    }


    private val notificationManager by lazy {
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    private val contentIntent by lazy {
        PendingIntent.getActivity(
            context,
            0,
            Intent(context, MainActivity::class.java),
            0
        )
    }
    private val notificationBuilder: NotificationCompat.Builder by lazy {
        NotificationCompat.Builder(context, NOTIFICATION_ID)
            .setContentTitle(context.getString(R.string.app_name))
            .setSound(null)
            .setContentIntent(contentIntent)
            .setSmallIcon(R.drawable.btc_notification)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() =
        NotificationChannel(
            NOTIFICATION_ID,
            context.getString(R.string.app_name),
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = context.packageName
        }

    fun getNotification(message: String): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(createChannel())
        }

        return notificationBuilder.setContentText(message).build()
    }
}