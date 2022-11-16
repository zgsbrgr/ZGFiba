@file:Suppress("MagicNumber")
package com.zgsbrgr.demo.fiba.data

import android.util.Log
import com.zgsbrgr.demo.fiba.domain.MatchEvent
import com.zgsbrgr.demo.fiba.domain.MatchEventTeam
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class GameInfoRepositoryImpl : GameInfoRepository {

    override fun fetchGameInfo(id: String): Flow<MatchEvent> = flow {
        val randomEvent = MatchEventTeam.randomMatchEvent()
        while (true) {
            delay(600)
            emit(
                MatchEvent(
                    id,
                    randomEvent.eventRes,
                    randomEvent.team,
                    randomEvent.iconRes
                )
            )
            Log.d("GI", "emitting")
        }
    }.flowOn(Dispatchers.IO)
}
