package com.zgsbrgr.demo.fiba.data

import com.zgsbrgr.demo.fiba.domain.Section
import kotlinx.coroutines.flow.Flow

interface HomeRepository {

    fun loadDataForHome(): Flow<List<Section>>

}
