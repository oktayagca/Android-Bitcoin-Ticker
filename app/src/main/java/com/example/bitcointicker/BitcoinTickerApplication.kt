package com.example.bitcointicker

import android.app.Application
import android.content.Intent
import com.example.bitcointicker.service.CoinsRefreshService
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BitcoinTickerApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        val intent = Intent(this, CoinsRefreshService::class.java)
        intent.putExtra("SERVICE_COMMAND","START_SERVICE")
        startService(intent)
    }

    override fun onTerminate() {
        super.onTerminate()
    }
}