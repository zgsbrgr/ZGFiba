package com.zgsbrgr.demo.fiba.data

import com.zgsbrgr.demo.fiba.di.Dispatcher
import com.zgsbrgr.demo.fiba.di.ZGFibaDispatchers
import com.zgsbrgr.demo.fiba.domain.Player
import com.zgsbrgr.demo.fiba.network.ZGFibaNetworkDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RosterRepositoryImpl @Inject constructor(
    private val networkDataSource: ZGFibaNetworkDataSource,
    @Dispatcher(ZGFibaDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) : RosterRepository {

    override fun fetchRosterForTeam(teamId: String): Flow<List<Player>> = flow {
        emit(
            networkDataSource.getTeamRoster(teamId).map {
                it.toRoster()
            }
        )
    }.flowOn(ioDispatcher)

    override fun fetchRosterForTeamExceptStatistics(teamId: String): Flow<List<Player>> = flow {
        emit(
            networkDataSource.getTeamRoster(teamId).map { it.toRoster() }.filter {
                it.age != null
            }
        )
    }.flowOn(ioDispatcher)
}
