package com.example.bitcointicker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.bitcointicker.data.entities.local.CoinEntity

@Database(
    version = 1,
    entities = [CoinEntity::class]
)
abstract class BitcoinTickerDatabase:RoomDatabase() {
    abstract fun dbDao():BitcoinTickerDao
}