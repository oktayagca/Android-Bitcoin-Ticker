package com.example.bitcointicker.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.bitcointicker.data.entities.local.CoinEntity
import com.example.bitcointicker.data.entities.remote.CoinDetailResponse
import com.example.bitcointicker.data.local.LocalDataSource
import com.example.bitcointicker.data.remote.RemoteDataSource
import com.example.bitcointicker.data.utils.IS_AUTH
import com.example.bitcointicker.data.utils.REFRESH_TIME
import com.example.bitcointicker.data.utils.performGetAllCoinsNetworkOperation
import com.example.bitcointicker.data.utils.performNetworkOperation
import com.example.bitcointicker.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class Repository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFireStore: FirebaseFirestore
) {
    fun getCoins() = performGetAllCoinsNetworkOperation(
        call = {
            remoteDataSource.getCoins()
        },
        saveAllCoinsToDb = {
            localDataSource.saveAllCoinToDb(it)

        }
    )

    suspend fun getCoinNameCurrentPriceChanged():ArrayList<String?> {
        var favoriteList: List<CoinEntity>?
        val coinDetailList  = arrayListOf<CoinDetailResponse?>()
        val coinNameList = arrayListOf<String?>()
        withContext(Dispatchers.IO) {
            favoriteList = localDataSource.getFavoriteCoins()
            favoriteList?.forEach {
                coinDetailList.add(remoteDataSource.getCoinDetailById(it.id).data)
            }
        }

        coinDetailList.forEachIndexed { index, coinDetailResponse ->
            if(favoriteList?.get(index)?.currentPrice != coinDetailResponse?.marketData?.currentPrice?.usd )
            {
                coinNameList.add(favoriteList?.get(index)?.name)
            }
        }

        return coinNameList
    }

    suspend fun searchCoin(keyword: String) = Resource.success(localDataSource.searchCoin(keyword))

    fun getCoinDetailById(coinId: String) = performNetworkOperation {
        remoteDataSource.getCoinDetailById(coinId)
    }

    fun getCoinChart(coinId: String) = performNetworkOperation {
        remoteDataSource.getCoinChart(coinId)
    }

    fun getRefreshTime() = localDataSource.getString(REFRESH_TIME)

    fun setRefreshTime(time: String) {
        localDataSource.saveString(REFRESH_TIME, time)
    }

    fun isAuthUser() =
        localDataSource.getBoolean(IS_AUTH)

    fun setIsAuthUser(isAuth: Boolean) {
        localDataSource.saveBoolean(IS_AUTH, isAuth)
    }

    fun firebaseRegister(email: String, password: String) =
        liveData(Dispatchers.IO) {
            emit(Resource.loading())
            try {
                firebaseAuth.createUserWithEmailAndPassword(email, password).await()
                emit(Resource.success(firebaseAuth.currentUser))
            } catch (exc: FirebaseAuthInvalidCredentialsException) {
                emit(Resource.error(exc.message!!, null, null))
            }
        }

    fun firebaseLogin(email: String, password: String) =
        liveData(Dispatchers.IO) {
            emit(Resource.loading())
            try {
                firebaseAuth.signInWithEmailAndPassword(email, password).await()
                emit(Resource.success(firebaseAuth.currentUser))
            } catch (exc: FirebaseAuthInvalidCredentialsException) {
                emit(Resource.error(exc.message!!, null, null))
            }

        }

    fun logOut() {
        firebaseAuth.signOut()
    }

    fun fireStoreSaveCoinToFavoriteAndUpdateDbData(coinId: String): LiveData<Resource<String>> {
        val data: MutableMap<String, Any> = HashMap()
        data["coinId"] = coinId
        return liveData(Dispatchers.IO) {
            emit(Resource.loading())
            try {
                firebaseFireStore.collection("users")
                    .document(firebaseAuth.currentUser!!.uid).collection("favorites")
                    .document(coinId)
                    .set(data).await()
                localDataSource.setFavorite(coinId, true)
                emit(Resource.success(""))
            } catch (exc: FirebaseFirestoreException) {
                emit(Resource.error(exc.message!!, null, null))
            }

        }
    }


    fun fireStoreRemoveCoinToFavoriteAndUpdateDbData(coinId: String) =
        liveData(Dispatchers.IO) {
            emit(Resource.loading())
            try {
                firebaseFireStore.collection("users")
                    .document(firebaseAuth.currentUser!!.uid).collection("favorites")
                    .document(coinId).delete().await()
                localDataSource.setFavorite(coinId, false)
                emit(Resource.success(""))
            } catch (exc: FirebaseFirestoreException) {
                emit(Resource.error(exc.message!!, null, null))
            }

        }

    suspend fun isFavoriteGetFromDb(coinId: String) = localDataSource.isFavorite(coinId)

    fun getFavoritesList() =
        liveData(Dispatchers.IO) {
            emit(Resource.loading())
            try {
                val snapshot = firebaseFireStore.collection("users")
                    .document(firebaseAuth.currentUser!!.uid).collection("favorites")
                    .get().await()
                val coinIdList = arrayListOf<CoinEntity>()
                snapshot.documents.forEach {
                    coinIdList.add(localDataSource.getCoinById(it.data?.getValue("coinId") as String))
                }
                emit(Resource.success(coinIdList))
            } catch (exc: FirebaseFirestoreException) {
                emit(Resource.error(exc.message!!, null, null))
            }

        }
}