package com.example.bitcointicker.di

import android.content.Context
import androidx.room.Room
import com.example.bitcointicker.data.local.BitcoinTickerDao
import com.example.bitcointicker.data.local.BitcoinTickerDatabase
import com.example.bitcointicker.data.local.LocalDataSource
import com.example.bitcointicker.data.local.SharedPreferencesManager
import com.example.bitcointicker.data.remote.ApiService
import com.example.bitcointicker.data.remote.RemoteDataSource
import com.example.bitcointicker.data.repository.Repository
import com.example.bitcointicker.service.NotificationHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalDatabaseModule {
    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferencesManager =
        SharedPreferencesManager(context)

    @Singleton
    @Provides
    fun provideRemoteDataSource(
        apiService: ApiService
    ) : RemoteDataSource = RemoteDataSource(apiService)

    @Singleton
    @Provides
    fun provideLocalDataSource(
        sharedPreferencesManager: SharedPreferencesManager,
        dbDao: BitcoinTickerDao
    ) : LocalDataSource = LocalDataSource(sharedPreferencesManager, dbDao)

    @Singleton
    @Provides
    fun provideRepository(
        localDataSource: LocalDataSource,
        remoteDataSource: RemoteDataSource,
        firebaseAuth: FirebaseAuth,
        firebaseFirestore: FirebaseFirestore
    ): Repository {
        return Repository(remoteDataSource,localDataSource,firebaseAuth,firebaseFirestore)
    }

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
    ): BitcoinTickerDatabase {
        return Room.databaseBuilder(context, BitcoinTickerDatabase::class.java, "BitcoinTicker")
            .build()
    }

    @Provides
    @Singleton
    fun provideDao(database: BitcoinTickerDatabase): BitcoinTickerDao {
        return database.dbDao()
    }

    @Provides
    @Singleton
    fun provideNotificationHelper(@ApplicationContext context: Context): NotificationHelper {
        return NotificationHelper(context)
    }
}