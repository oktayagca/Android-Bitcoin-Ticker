package com.example.bitcointicker.data.entities.remote


import com.google.gson.annotations.SerializedName

data class CurrentPrice(
    @SerializedName("usd")
    val usd: Double? = null
)
