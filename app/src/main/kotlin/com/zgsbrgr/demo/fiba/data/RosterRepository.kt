package com.zgsbrgr.demo.fiba.data

import com.zgsbrgr.demo.fiba.domain.Player
import kotlinx.coroutines.flow.Flow

interface RosterRepository {

    fun fetchRosterForTeam(teamId: String): Flow<List<Player>>
}
