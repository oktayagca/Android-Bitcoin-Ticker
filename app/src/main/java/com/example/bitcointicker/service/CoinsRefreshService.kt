package com.example.bitcointicker.service

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationManagerCompat
import com.example.bitcointicker.data.repository.Repository
import com.example.bitcointicker.service.NotificationHelper.Companion.NOTIFICATION_ID
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class CoinsRefreshService : Service() {

    @Inject
    lateinit var repository: Repository

    @Inject
    lateinit var notification: NotificationHelper
    private lateinit var notificationManager: NotificationManagerCompat
    private var handler: Handler? = null
    private val refreshRunnable = Runnable {
        fetchData()
    }

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        handler = Handler(Looper.getMainLooper())
        notificationManager = NotificationManagerCompat.from(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        intent?.extras?.run {
            when (getSerializable("SERVICE_COMMAND") as String) {
                "START_SERVICE" -> fetchData()
                "STOP_SERVICE" -> endService()
                else -> return START_NOT_STICKY
            }
        }
        return START_NOT_STICKY
    }

    private fun fetchData() {
        CoroutineScope(Dispatchers.IO).launch {
            val result = repository.getCoinNameCurrentPriceChanged()
            result.forEach { coinName ->
                notificationManager.notify(
                    NOTIFICATION_ID.toInt(),
                    notification.getNotification("$coinName price has changed!!")
                )
            }
        }

        startRunnable()
    }

    override fun stopService(name: Intent?): Boolean {
        stopRunnable()
        return super.stopService(name)
    }

    private fun endService() {
        stopForeground(true)
        stopSelf()
    }

    private fun stopRunnable() {
        handler!!.removeCallbacks(refreshRunnable)
        handler = null
    }


    private fun startRunnable() {
        if (handler != null) {
            handler!!.postDelayed(refreshRunnable, 45000)
        }
    }

}