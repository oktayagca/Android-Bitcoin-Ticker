package com.example.bitcointicker.data.remote

import com.example.bitcointicker.data.entities.remote.CoinChartResponse
import com.example.bitcointicker.data.entities.remote.CoinDetailResponse
import com.example.bitcointicker.data.entities.remote.CoinsResponse
import com.example.bitcointicker.data.utils.NetworkConst
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET(NetworkConst.GET_COINS)
    suspend fun getCoins(): Response<CoinsResponse>

    @GET("coins/{id}")
    suspend fun getCoinDetailById(@Path(NetworkConst.GET_COIN_DETAIL_BY_ID) coinId: String): Response<CoinDetailResponse>

    @GET(NetworkConst.GET_COIN_CHART_DATA)
    suspend fun getCoinChart(
        @Path("id") coinId: String,
    ): Response<CoinChartResponse>
}
