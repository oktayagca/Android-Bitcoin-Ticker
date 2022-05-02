package com.example.bitcointicker.data.utils

class NetworkConst{
    companion object{
        const val BASE_URL = "https://api.coingecko.com/api/v3/"
        const val GET_COINS = "coins/markets?vs_currency=usd"
        const val GET_COIN_DETAIL_BY_ID = "id"
        const val GET_COIN_CHART_DATA = "coins/{id}/market_chart/?vs_currency=usd&&days=1"
    }
}