package com.zgsbrgr.demo.fiba.data

import com.zgsbrgr.demo.fiba.domain.Section
import com.zgsbrgr.demo.fiba.network.ZGFibaNetworkDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val networkDataSource: ZGFibaNetworkDataSource
) : HomeRepository {

    override fun loadDataForHome(): Flow<List<Section>> = flow {
        emit(
            networkDataSource.getResults().map {
                it.toDomain()
            }.asReversed()
        )
    }.flowOn(Dispatchers.IO)
}
