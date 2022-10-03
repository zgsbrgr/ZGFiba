package com.zgsbrgr.demo.fiba.di

import com.zgsbrgr.demo.fiba.data.Api
import com.zgsbrgr.demo.fiba.data.GameInfoRepository
import com.zgsbrgr.demo.fiba.data.GameInfoRepositoryImpl
import com.zgsbrgr.demo.fiba.data.HomeRepository
import com.zgsbrgr.demo.fiba.data.HomeRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @Provides
    fun provideHomeRepository(api: Api): HomeRepository = HomeRepositoryImpl(api)
}

@Module
@InstallIn(FragmentComponent::class)
object GameInfoRepoModule {

    @Provides
    fun provideGameInfoRepository(): GameInfoRepository = GameInfoRepositoryImpl()
}
