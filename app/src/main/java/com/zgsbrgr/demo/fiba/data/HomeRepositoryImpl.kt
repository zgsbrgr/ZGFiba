package com.zgsbrgr.demo.fiba.data

import com.zgsbrgr.demo.fiba.domain.Section
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class HomeRepositoryImpl @Inject constructor(
    private val api: Api
): HomeRepository {

    override fun loadDataForHome(): Flow<List<Section>> = flow{
        emit(api.fetchResults().sections.map {
            it.toDomain()
        }.asReversed())

    }.flowOn(Dispatchers.IO)

}