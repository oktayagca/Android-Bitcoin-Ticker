package com.example.bitcointicker.data.entities.remote


import com.google.gson.annotations.SerializedName

data class CoinChartResponse(
    @SerializedName("market_caps")
    val marketCaps: List<List<Double>>,
    @SerializedName("prices")
    val prices: List<List<Double>>,
    @SerializedName("total_volumes")
    val totalVolumes: List<List<Double>>
)