package com.example.bitcointicker.data.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.bitcointicker.data.entities.local.CoinEntity
import com.example.bitcointicker.data.entities.remote.CoinsResponse
import com.example.bitcointicker.utils.Resource
import kotlinx.coroutines.Dispatchers

fun <T> performNetworkOperation(call: suspend () -> Resource<T>): LiveData<Resource<T>> {
    return liveData(Dispatchers.IO) {
        emit(Resource.loading())
        val networkCall = call.invoke()
        if (networkCall.status == Resource.Status.SUCCESS) {
            val data = networkCall.data!!
            emit(Resource.success(data))
        } else if (networkCall.status == Resource.Status.ERROR) {
            emit(
                Resource.error(
                    networkCall.message!!,
                    networkCall.data,
                    networkCall.code
                )
            )
        }
    }
}

fun <T> performGetAllCoinsNetworkOperation(
    call: suspend () -> Resource<T>,
    saveAllCoinsToDb:suspend (list: List<CoinEntity>) -> Unit,
): LiveData<Resource<List<CoinEntity>>> {
    return liveData(Dispatchers.IO) {
        emit(Resource.loading())
        val networkCall = call.invoke()
        if (networkCall.status == Resource.Status.SUCCESS) {
            val data = networkCall.data!!
            if (data is CoinsResponse) {
                val list = arrayListOf<CoinEntity>()
                data.toList().forEach {
                    list.add(it.toLocalCoins())
                }
                saveAllCoinsToDb(list)
                emit(Resource.success(list))
            }
        } else if (networkCall.status == Resource.Status.ERROR) {
            emit(
                Resource.error(
                    networkCall.message!!,
                    null,
                    networkCall.code
                )
            )
        }
    }
}