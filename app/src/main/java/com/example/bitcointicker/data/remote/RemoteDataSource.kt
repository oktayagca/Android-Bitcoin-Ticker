package com.example.bitcointicker.data.remote

import com.example.bitcointicker.data.utils.BaseDataSource

class RemoteDataSource (
     private val apiService: ApiService
) : BaseDataSource() {

    suspend fun getCoins() = getResult {
        apiService.getCoins()
    }
    suspend fun getCoinDetailById(coinId: String) = getResult {
        apiService.getCoinDetailById(coinId)
    }
    suspend fun getCoinChart(coinId: String) = getResult {
        apiService.getCoinChart(coinId)
    }

}