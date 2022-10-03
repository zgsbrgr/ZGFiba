package com.zgsbrgr.demo.fiba.data

import com.zgsbrgr.demo.fiba.domain.MatchEvent
import kotlinx.coroutines.flow.Flow

interface GameInfoRepository {

    fun fetchGameInfo(id: String): Flow<MatchEvent>
}
