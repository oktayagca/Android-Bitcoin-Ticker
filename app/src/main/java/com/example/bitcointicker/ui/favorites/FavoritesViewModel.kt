package com.example.bitcointicker.ui.favorites

import com.example.bitcointicker.data.repository.Repository
import com.example.bitcointicker.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val  repository: Repository
):BaseViewModel() {

    fun isAuthUser() = repository.isAuthUser()

    fun getFavoritesList() =
        repository.getFavoritesList()

}