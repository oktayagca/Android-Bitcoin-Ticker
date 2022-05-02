package com.example.bitcointicker.data.entities.remote


import com.example.bitcointicker.data.entities.remote.CurrentPrice
import com.google.gson.annotations.SerializedName

data class MarketData(
    @SerializedName("current_price")
    val currentPrice: CurrentPrice?= null
)