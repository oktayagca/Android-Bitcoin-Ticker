package com.example.bitcointicker.data.entities.remote


import com.google.gson.annotations.SerializedName

data class CoinDetailResponse(
    @SerializedName("description")
    val description: Description,
    @SerializedName("hashing_algorithm")
    val hashingAlgorithm: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("image")
    val image: İmage,
    @SerializedName("market_cap_rank")
    val marketCapRank: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("market_data")
    val marketData: MarketData?= null

)