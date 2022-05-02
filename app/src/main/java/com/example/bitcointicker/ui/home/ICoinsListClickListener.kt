package com.example.bitcointicker.ui.home

import com.example.bitcointicker.data.entities.local.CoinEntity

interface ICoinsListClickListener {
    fun onClick(item: CoinEntity) {
    }
}