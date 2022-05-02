package com.example.bitcointicker.data.entities.local

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "coins")
data class CoinEntity(
    @PrimaryKey
    var id: String,
    @ColumnInfo(name = "symbol")
    var symbol: String,
    @ColumnInfo(name = "ath")
    var ath: Double?,
    @ColumnInfo(name = "ath_change_percentage")
    var athChangePercentage: Double?,
    @ColumnInfo(name = "ath_date")
    var athDate: String?,
    @ColumnInfo(name = "atl")
    var atl: Double?,
    @ColumnInfo(name = "atl_change_percentage")
    var atlChangePercentage: Double?,
    @ColumnInfo(name = "atl_date")
    var atlDate: String?,
    @ColumnInfo(name = "circulating_supply")
    var circulatingSupply: Double?,
    @ColumnInfo(name = "current_price")
    var currentPrice: Double?,
    @ColumnInfo(name = "fully_diluted_valuation")
    var fullyDilutedValuation: Long?,
    @ColumnInfo(name = "high_24h")
    var high24h: Double?,
    @ColumnInfo(name = "image")
    var image: String?,
    @ColumnInfo(name = "last_updated")
    var lastUpdated: String?,
    @ColumnInfo(name = "low_24h")
    var low24h: Double?,
    @ColumnInfo(name = "market_cap")
    var marketCap: Long?,
    @ColumnInfo(name = "market_cap_change_24h")
    var marketCapChange24h: Double?,
    @ColumnInfo(name = "market_cap_change_percentage_24h")
    var marketCapChangePercentage24h: Double?,
    @ColumnInfo(name = "market_cap_rank")
    var marketCapRank: Int?,
    @ColumnInfo(name = "max_supply")
    var maxSupply: Double?,
    @ColumnInfo(name = "name")
    var name: String?,
    @ColumnInfo(name = "price_change_24h")
    var priceChange24h: Double?,
    @ColumnInfo(name = "price_change_percentage_24h")
    var priceChangePercentage24h: Double?,
    @ColumnInfo(name = "total_supply")
    var totalSupply: Double?,
    @ColumnInfo(name = "total_volume")
    var totalVolume: Double?,
    @ColumnInfo(name = "is_favorite")
    var isFavorite: Boolean = false,
)