package com.zgsbrgr.demo.fiba.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zgsbrgr.demo.fiba.asResult
import com.zgsbrgr.demo.fiba.data.RosterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import com.zgsbrgr.demo.fiba.Result.Loading
import com.zgsbrgr.demo.fiba.Result.Error
import com.zgsbrgr.demo.fiba.Result.Success
import com.zgsbrgr.demo.fiba.domain.Player

@HiltViewModel
class RosterViewModel @Inject constructor(
    private val rosterRepository: RosterRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _uiState = MutableStateFlow(RosterUIState(loading = false))
    val uiState: StateFlow<RosterUIState> = _uiState.asStateFlow()

    init {
        val result = rosterRepository
            .fetchRosterForTeam(
                savedStateHandle.get<String>("teamId")!!
            ).asResult()
        result.onEach { fetchResult->
            _uiState.update {
                when(fetchResult) {
                    is Loading -> {
                        it.copy(loading = true)
                    }
                    is Error -> {
                        it.copy(loading = false, error = fetchResult.exception?.message.toString())
                    }
                    is Success -> {
                        it.copy(loading = false, data = fetchResult.data)
                    }
                }
            }
        }.launchIn(viewModelScope)

    }

}
data class RosterUIState(
    val loading: Boolean = false,
    val data: List<Player> = emptyList(),
    val error: String? = null
)