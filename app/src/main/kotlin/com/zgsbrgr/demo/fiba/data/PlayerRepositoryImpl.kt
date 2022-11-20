@file:Suppress("TooGenericExceptionCaught")
package com.zgsbrgr.demo.fiba.data

import com.zgsbrgr.demo.fiba.Result
import com.zgsbrgr.demo.fiba.di.Dispatcher
import com.zgsbrgr.demo.fiba.di.ZGFibaDispatchers
import com.zgsbrgr.demo.fiba.domain.Player
import com.zgsbrgr.demo.fiba.network.ZGFibaNetworkDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PlayerRepositoryImpl @Inject constructor(
    private val networkDataSource: ZGFibaNetworkDataSource,
    @Dispatcher(ZGFibaDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) : PlayerRepository {

    override suspend fun fetchPlayer(teamId: String, positionInTeam: Int): Result<Player> =
        withContext(ioDispatcher) {
            try {
                Result.Success(networkDataSource.getTeamRoster(teamId)[positionInTeam].toRoster())
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
}
