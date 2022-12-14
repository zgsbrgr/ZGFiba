package com.zgsbrgr.demo.fiba.di

import com.zgsbrgr.demo.fiba.data.GameInfoRepository
import com.zgsbrgr.demo.fiba.data.GameInfoRepositoryImpl
import com.zgsbrgr.demo.fiba.data.HomeRepository
import com.zgsbrgr.demo.fiba.data.HomeRepositoryImpl
import com.zgsbrgr.demo.fiba.data.PlayerRepository
import com.zgsbrgr.demo.fiba.data.PlayerRepositoryImpl
import com.zgsbrgr.demo.fiba.data.RosterRepository
import com.zgsbrgr.demo.fiba.data.RosterRepositoryImpl
import com.zgsbrgr.demo.fiba.di.ZGFibaDispatchers.IO
import com.zgsbrgr.demo.fiba.network.ZGFibaNetworkDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideHomeRepository(
        networkDataSource: ZGFibaNetworkDataSource,
        @Dispatcher(IO) ioDispatcher: CoroutineDispatcher,
    ): HomeRepository =
        HomeRepositoryImpl(networkDataSource, ioDispatcher)

    @Provides
    @Singleton
    fun provideRosterRepository(
        networkDataSource: ZGFibaNetworkDataSource,
        @Dispatcher(IO) ioDispatcher: CoroutineDispatcher
    ): RosterRepository =
        RosterRepositoryImpl(networkDataSource, ioDispatcher)

    @Provides
    @Singleton
    fun providePlayerRepository(
        networkDataSource: ZGFibaNetworkDataSource,
        @Dispatcher(IO) ioDispatcher: CoroutineDispatcher
    ): PlayerRepository =
        PlayerRepositoryImpl(networkDataSource, ioDispatcher)

    @Provides
    @Singleton
    fun provideGameInfoRepository(): GameInfoRepository = GameInfoRepositoryImpl()
}
