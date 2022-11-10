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

    // TODO add proper error handling
    override fun fetchRosterForTeam(teamId: String): Flow<List<Player>> = flow {
        emit(
            try {
                networkDataSource.getTeamRoster(teamId).map {
                    it.toRoster()
                }
            }catch (e: Exception) {
                emptyList()
            }

        )
    }.flowOn(ioDispatcher)

    // TODO add proper error handling
    override fun fetchRosterForTeamExceptStatistics(teamId: String): Flow<List<Player>> = flow {
        emit(
            try {
                networkDataSource.getTeamRoster(teamId).map { it.toRoster() }.filter {
                    it.age != null
                }
            }catch (e: Exception) {
                emptyList()
            }

        )
    }.flowOn(ioDispatcher)
}
