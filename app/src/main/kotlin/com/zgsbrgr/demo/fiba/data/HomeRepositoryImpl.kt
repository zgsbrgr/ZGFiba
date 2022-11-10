package com.zgsbrgr.demo.fiba.data

import com.zgsbrgr.demo.fiba.di.Dispatcher
import com.zgsbrgr.demo.fiba.di.ZGFibaDispatchers.IO
import com.zgsbrgr.demo.fiba.domain.Section
import com.zgsbrgr.demo.fiba.network.ZGFibaNetworkDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val networkDataSource: ZGFibaNetworkDataSource,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) : HomeRepository {

    override fun loadDataForHome(): Flow<List<Section>> = flow {
        emit(
            try {
                networkDataSource.getResults().map {
                    it.toDomain()
                }.asReversed()
            }catch (e: Exception) {
                emptyList()
            }

        )
    }.flowOn(ioDispatcher)
}
