@file:Suppress("MagicNumber")
package com.zgsbrgr.demo.fiba.data

import com.zgsbrgr.demo.fiba.domain.MatchEvent
import com.zgsbrgr.demo.fiba.domain.MatchEventTeam
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.isActive

class GameInfoRepositoryImpl : GameInfoRepository {

    override fun fetchGameInfo(id: String): Flow<MatchEvent> = flow {

        while (currentCoroutineContext().isActive) {
            val randomEvent = MatchEventTeam.randomMatchEvent()
            delay(600)
            emit(
                MatchEvent(
                    id,
                    randomEvent.eventRes,
                    randomEvent.team,
                    randomEvent.iconRes
                )
            )
        }
    }.flowOn(Dispatchers.IO)
}
