package com.example.bitcointicker.ui.coinDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.bitcointicker.data.repository.Repository
import com.example.bitcointicker.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoinDetailViewModel @Inject constructor(
    private val repository: Repository
): BaseViewModel(){

    private var isFavorite= MutableLiveData<Boolean>()

    fun getCoinDetailFromNetwork(coinId:String)=
         repository.getCoinDetailById(coinId)

    fun getCoinChart(coinId: String) =
        repository.getCoinChart(coinId)

    fun getRefreshTime() =
        repository.getRefreshTime()

    fun setRefreshTime(time:String){
        repository.setRefreshTime(time)
    }

    fun observeIsFavorite(coinId:String): LiveData<Boolean> {
        if(isFavorite.value == null){
            isFavoriteGetFromDb(coinId)
        }
        return isFavorite
    }
    fun saveCoinToFavorite(coinId:String) = repository.fireStoreSaveCoinToFavoriteAndUpdateDbData(coinId)

    fun removeCoinToFavorite(coinId:String) =repository.fireStoreRemoveCoinToFavoriteAndUpdateDbData(coinId)

    private fun isFavoriteGetFromDb(coinId: String){
        viewModelScope.launch {
            isFavorite.value = repository.isFavoriteGetFromDb(coinId)
        }
    }

    fun isAuthUser():Boolean{
        return repository.isAuthUser()
    }

}