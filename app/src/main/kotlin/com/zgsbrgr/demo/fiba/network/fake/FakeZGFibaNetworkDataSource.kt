package com.zgsbrgr.demo.fiba.network.fake

import com.zgsbrgr.demo.fiba.data.RosterDto
import com.zgsbrgr.demo.fiba.data.SectionDto
import com.zgsbrgr.demo.fiba.di.Dispatcher
import com.zgsbrgr.demo.fiba.di.ZGFibaDispatchers.IO
import com.zgsbrgr.demo.fiba.network.ZGFibaNetworkDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class FakeZGFibaNetworkDataSource @Inject constructor(
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
    private val networkJson: Json
) : ZGFibaNetworkDataSource {

    override suspend fun getResults(): List<SectionDto> =
        withContext(ioDispatcher) {
            networkJson.decodeFromString(FakeDataSource.sectionData)
        }

    override suspend fun getTeamRoster(teamId: String?): List<RosterDto> =
        withContext(ioDispatcher) {
            networkJson.decodeFromString(FakeDataSource.rosterData)
        }
}
