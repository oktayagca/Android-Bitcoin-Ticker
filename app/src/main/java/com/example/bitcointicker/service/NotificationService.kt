package com.example.bitcointicker.service

import android.app.Service
import android.content.Intent
import android.os.IBinder

class NotificationService : Service() {

    private val helper by lazy { NotificationHelper(this) }

    override fun onBind(p0: Intent?): IBinder?  = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        intent?.extras?.run {
            when (getSerializable("SERVICE_COMMAND") as String) {
                "SEND_NOTIFICATION" -> sendNotification(getSerializable("MESSAGE") as String)
                "STOP_SERVICE"->endService()
                else -> return START_NOT_STICKY
            }
        }
        return START_NOT_STICKY
    }

    private fun sendNotification(message: String){
        startForeground(NotificationHelper.NOTIFICATION_ID.toInt(), helper.getNotification(message))
    }

    private fun endService(){
        stopForeground(true)
        stopSelf()
    }

}