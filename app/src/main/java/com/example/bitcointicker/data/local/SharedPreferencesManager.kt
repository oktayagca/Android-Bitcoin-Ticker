package com.example.bitcointicker.data.local

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesManager(context: Context) {


    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("BitcoinTicker", Context.MODE_PRIVATE)

    fun saveString(key:String, value: String?) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun getString(key:String): String? {
        return sharedPreferences.getString(key, "")
    }

    fun saveBoolean(key: String,value: Boolean){
        sharedPreferences.edit().putBoolean(key,value).apply()
    }

    fun getBoolean(key:String): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }

}