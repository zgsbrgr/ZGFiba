@file:Suppress("MagicNumber")
package com.zgsbrgr.demo.fiba.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zgsbrgr.demo.fiba.data.RosterRepository
import com.zgsbrgr.demo.fiba.domain.Player
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class RosterViewModel @Inject constructor(
    rosterRepository: RosterRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val rosterUIState: StateFlow<RosterUiState> = combine(
        rosterRepository.fetchRosterForTeam(
            savedStateHandle.get<String>("homeTeamId")!!
        ),
        rosterRepository.fetchRosterForTeam(
            savedStateHandle.get<String>("awayTeamId")!!
        )
    ) { homeRoster, awayRoster ->

        val size = if (homeRoster.size > awayRoster.size) homeRoster.size else awayRoster.size
        val homeAndAwayRosters = mutableListOf<Pair<Player?, Player?>>()
        for (i in 0 until size) {
            homeAndAwayRosters.add(Pair(getPlayerFromListOrNull(i, homeRoster), getPlayerFromListOrNull(i, awayRoster)))
        }
        RosterUiState.Rosters(
            homeAndAwayRosters
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = RosterUiState.Loading
    )

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
// data class RosterUIState(
//    val loading: Boolean = false,
//    val data: List<Player> = emptyList(),
//    val error: String? = null
// )
