package com.zgsbrgr.demo.fiba.data

import com.zgsbrgr.demo.fiba.Result
import com.zgsbrgr.demo.fiba.domain.Player

interface PlayerRepository {

    suspend fun fetchPlayer(teamId: String, positionInTeam: Int): Result<Player>
}
