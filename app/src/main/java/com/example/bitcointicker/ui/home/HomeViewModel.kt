package com.example.bitcointicker.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.bitcointicker.data.entities.local.CoinEntity
import com.example.bitcointicker.data.repository.Repository
import com.example.bitcointicker.ui.base.BaseViewModel
import com.example.bitcointicker.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository
): BaseViewModel() {
    private var coinList= MutableLiveData<Resource<List<CoinEntity>>>()

    fun observeCoinList(): LiveData<Resource<List<CoinEntity>>> {
        if(coinList.value == null){
            getCoinList()
        }
        return coinList
    }

    private fun getCoinList() {
        coinList= repository.getCoins() as MutableLiveData<Resource<List<CoinEntity>>>
    }
    fun searchCoin(keyword: String){
        viewModelScope.launch {
            coinList.value = repository.searchCoin(keyword)
        }
    }

    fun isAuthUser():Boolean{
        return repository.isAuthUser()
    }

    fun logOut(){
        repository.logOut()
        repository.setIsAuthUser(false)
    }
}