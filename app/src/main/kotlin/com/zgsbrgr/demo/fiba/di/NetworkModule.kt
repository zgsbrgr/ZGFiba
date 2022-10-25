package com.zgsbrgr.demo.fiba.di

import com.zgsbrgr.demo.fiba.ZGFibaApp
import com.zgsbrgr.demo.fiba.data.util.ConnectivityManagerNetworkMonitor
import com.zgsbrgr.demo.fiba.data.util.NetworkMonitor
import com.zgsbrgr.demo.fiba.network.ZGFibaNetworkDataSource
import com.zgsbrgr.demo.fiba.network.retrofit.RetrofitZGFibaNetwork
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface NetworkModule {

    @Binds
    fun bindZGFibaNetwork(retrofitZGFibaNetwork: RetrofitZGFibaNetwork): ZGFibaNetworkDataSource

    @Binds
    fun bindsNetworkMonitor(
        networkMonitor: ConnectivityManagerNetworkMonitor
    ): NetworkMonitor

    companion object {
        @Provides
        @Singleton
        fun providesNetworkJson(): Json = Json {
            ignoreUnknownKeys = true
        }

        @Provides
        @Singleton
        fun provideApplication(): ZGFibaApp = ZGFibaApp()
    }
}
