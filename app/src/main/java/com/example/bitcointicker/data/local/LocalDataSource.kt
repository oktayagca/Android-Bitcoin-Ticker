package com.example.bitcointicker.data.local

import com.example.bitcointicker.data.entities.local.CoinEntity

class LocalDataSource(
    private val sharedPreferencesManager: SharedPreferencesManager,
    private val dbDao: BitcoinTickerDao
) {
    fun getString(key:String)=
        sharedPreferencesManager.getString(key)

    fun saveString(key:String,value:String?)=
        sharedPreferencesManager.saveString(key,value)


    fun saveBoolean(key: String,value: Boolean) = sharedPreferencesManager.saveBoolean(key,value)

    fun getBoolean(key: String) = sharedPreferencesManager.getBoolean(key)

    suspend fun saveAllCoinToDb(list: List<CoinEntity>){
        dbDao.saveAllCoins(list)
    }
    suspend fun searchCoin(keyword: String)=
      dbDao.searchCoin(keyword)

    suspend fun setFavorite(coinId: String,isFavorite:Boolean)=
        dbDao.setFavorite(coinId,isFavorite)

    suspend fun isFavorite(coinId: String)=
        dbDao.isFavorite(coinId)

    suspend fun getFavoriteCoins()=
        dbDao.getFavoriteCoins()


    suspend fun getCoinById(coinId: String)=
        dbDao.getCoinById(coinId)


}