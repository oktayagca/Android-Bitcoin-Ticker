package com.example.bitcointicker.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bitcointicker.data.entities.local.CoinEntity

@Dao
interface BitcoinTickerDao {

    @Query("SELECT * FROM coins WHERE is_favorite = 1")
    suspend fun getFavoriteCoins(): List<CoinEntity>

    @Query("SELECT * FROM coins WHERE id  = :coinId")
    suspend fun getCoinById(coinId: String): CoinEntity

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveAllCoins(list: List<CoinEntity>)

    @Query("UPDATE coins SET is_favorite = :isFavorite WHERE id = :coinId")
    suspend fun setFavorite(coinId: String, isFavorite: Boolean)

    @Query("SELECT is_favorite FROM coins WHERE id = :coinId")
    suspend fun isFavorite(coinId: String):Boolean

    @Query("SELECT * FROM coins where name like '%' || :keyword || '%' or symbol like '%' || :keyword || '%'")
    suspend fun searchCoin(keyword: String): List<CoinEntity>

}