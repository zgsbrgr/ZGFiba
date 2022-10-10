package com.zgsbrgr.demo.fiba.di

import com.zgsbrgr.demo.fiba.data.GameInfoRepository
import com.zgsbrgr.demo.fiba.data.GameInfoRepositoryImpl
import com.zgsbrgr.demo.fiba.data.HomeRepository
import com.zgsbrgr.demo.fiba.data.HomeRepositoryImpl
import com.zgsbrgr.demo.fiba.data.RosterRepository
import com.zgsbrgr.demo.fiba.data.RosterRepositoryImpl
import com.zgsbrgr.demo.fiba.network.ZGFibaNetworkDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @Provides
    fun provideHomeRepository(
        networkDataSource: ZGFibaNetworkDataSource,
        @Dispatcher(ZGFibaDispatchers.IO) ioDispatcher: CoroutineDispatcher,
    ): HomeRepository =
        HomeRepositoryImpl(networkDataSource, ioDispatcher)

    @Provides
    fun provideRosterRepository(
        networkDataSource: ZGFibaNetworkDataSource,
        @Dispatcher(ZGFibaDispatchers.IO) ioDispatcher: CoroutineDispatcher
    ): RosterRepository =
        RosterRepositoryImpl(networkDataSource, ioDispatcher)
}

@Module
@InstallIn(FragmentComponent::class)
object GameInfoRepoModule {

    @Provides
    fun provideGameInfoRepository(): GameInfoRepository = GameInfoRepositoryImpl()
}
