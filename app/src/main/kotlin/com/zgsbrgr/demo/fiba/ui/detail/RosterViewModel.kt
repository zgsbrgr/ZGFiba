@file:Suppress("MagicNumber")
package com.zgsbrgr.demo.fiba.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zgsbrgr.demo.fiba.Result
import com.zgsbrgr.demo.fiba.asResult
import com.zgsbrgr.demo.fiba.data.RosterRepository
import com.zgsbrgr.demo.fiba.domain.Player
import com.zgsbrgr.demo.fiba.domain.Team
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class RosterViewModel @Inject constructor(
    private val rosterRepository: RosterRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val rosterUIState: StateFlow<RosterUiState> =
        rosterUiStateStream().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = RosterUiState.Loading
        )

    private fun rosterUiStateStream(): Flow<RosterUiState> {

        return combine(
            rosterRepository.fetchRosterForTeamExceptStatistics(
                savedStateHandle.get<Team>("homeTeam")?.id!!
            ),
            rosterRepository.fetchRosterForTeamExceptStatistics(
                savedStateHandle.get<Team>("awayTeam")?.id!!
            ),
            ::Pair
        ).asResult()
            .map { homePlayersToAwayPlayers ->
                when (homePlayersToAwayPlayers) {
                    is Result.Success -> {
                        val (homePlayers, awayPlayers) = homePlayersToAwayPlayers.data
                        val size =
                            if (homePlayers.size > awayPlayers.size)
                                homePlayers.size
                            else awayPlayers.size
                        val homeAndAwayRosters = mutableListOf<Pair<Player?, Player?>>()
                        for (i in 0 until size) {
                            homeAndAwayRosters.add(
                                Pair(
                                    getPlayerFromListOrNull(i, homePlayers),
                                    getPlayerFromListOrNull(i, awayPlayers)
                                )
                            )
                        }
                        RosterUiState.Rosters(
                            homeAndAwayRosters
                        )
                    }
                    is Result.Loading -> {
                        RosterUiState.Loading
                    }
                    is Result.Error -> {
                        RosterUiState.Empty
                    }
                }
            }
    }

    private fun getPlayerFromListOrNull(position: Int, list: List<Player>): Player? {

        return if (position > (list.size - 1))
            null
        else
            list[position]
    }
}

sealed interface RosterUiState {
    object Loading : RosterUiState

    data class Rosters(
        val homeAndAwayRosters: List<Pair<Player?, Player?>>
    ) : RosterUiState

    object Empty : RosterUiState
}
