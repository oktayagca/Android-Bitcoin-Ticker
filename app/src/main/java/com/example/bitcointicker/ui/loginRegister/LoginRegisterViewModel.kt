package com.example.bitcointicker.ui.loginRegister

import com.example.bitcointicker.data.repository.Repository
import com.example.bitcointicker.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginRegisterViewModel @Inject constructor(
    private val repository: Repository
):BaseViewModel() {

    fun firebaseRegister(email:String,password:String) = repository.firebaseRegister(email,password)

    fun firebaseLogin(email:String,password:String) = repository.firebaseLogin(email,password)

    fun saveIsAuthUser(isAuth:Boolean){
        repository.setIsAuthUser(isAuth)
    }
}