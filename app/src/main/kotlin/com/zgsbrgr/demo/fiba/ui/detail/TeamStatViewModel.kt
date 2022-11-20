@file:Suppress("MagicNumber")
package com.zgsbrgr.demo.fiba.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zgsbrgr.demo.fiba.asResult
import com.zgsbrgr.demo.fiba.data.RosterRepository
import com.zgsbrgr.demo.fiba.domain.Team
import com.zgsbrgr.demo.fiba.domain.Teams
import com.zgsbrgr.demo.fiba.domain.toStatDataList
import com.zgsbrgr.demo.fiba.ui.adapter.StatData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class TeamStatViewModel @Inject constructor(
    private val rosterRepository: RosterRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _homeTeamStatData = mutableListOf<StatData>()
    private val homeTeamStatData: List<StatData> = _homeTeamStatData

    private val _awayTeamStatData = mutableListOf<StatData>()
    private val awayTeamStatData: List<StatData> = _awayTeamStatData

    val uiState: StateFlow<TeamStatUiState> =
        teamStatUiState()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = TeamStatUiState.Loading
            )

    fun changeTeamStatData(teams: Teams): List<StatData> {

        return if (teams == Teams.HOME) {
            homeTeamStatData
        } else {
            awayTeamStatData
        }
    }

    private fun teamStatUiState(): Flow<TeamStatUiState> {

        return combine(
            rosterRepository.fetchRosterForTeam(
                savedStateHandle.get<Team>("homeTeam")?.id!!
            ),
            rosterRepository.fetchRosterForTeam(
                savedStateHandle.get<Team>("awayTeam")?.id!!
            ),
            ::Pair
        ).asResult().map { homeTeamToAwayTeam ->
            when (homeTeamToAwayTeam) {
                is com.zgsbrgr.demo.fiba.Result.Success -> {
                    val (homeTeam, awayTeam) = homeTeamToAwayTeam.data
                    _homeTeamStatData.addAll(homeTeam.toStatDataList())
                    _awayTeamStatData.addAll(awayTeam.toStatDataList())
                    TeamStatUiState.Players(
                        homeTeamStatData,
                        awayTeamStatData
                    )
                }
                is com.zgsbrgr.demo.fiba.Result.Error -> {
                    TeamStatUiState.Empty
                }
                is com.zgsbrgr.demo.fiba.Result.Loading -> {
                    TeamStatUiState.Loading
                }
            }
        }
    }
}

sealed interface TeamStatUiState {
    object Loading : TeamStatUiState

    data class Players(
        val homePlayers: List<StatData>,
        val awayPlayers: List<StatData>
    ) : TeamStatUiState

    object Empty : TeamStatUiState
}
