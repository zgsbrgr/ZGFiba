package com.zgsbrgr.demo.fiba.di

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
    fun bindsZGFibaNetwork(
        zgFibaNetwork: RetrofitZGFibaNetwork
    ): ZGFibaNetworkDataSource

    companion object {
        @Provides
        @Singleton
        fun providesNetworkJson(): Json = Json {
            ignoreUnknownKeys = true
        }
    }
}
