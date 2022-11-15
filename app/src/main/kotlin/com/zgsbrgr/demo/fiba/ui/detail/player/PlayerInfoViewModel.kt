package com.zgsbrgr.demo.fiba.ui.detail.player

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zgsbrgr.demo.fiba.Result.Error
import com.zgsbrgr.demo.fiba.Result.Loading
import com.zgsbrgr.demo.fiba.Result.Success
import com.zgsbrgr.demo.fiba.data.PlayerRepository
import com.zgsbrgr.demo.fiba.domain.Player
import com.zgsbrgr.demo.fiba.domain.Team
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerInfoViewModel @Inject constructor(
    playerRepository: PlayerRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlayerUiState(loading = true))
    val uiState: StateFlow<PlayerUiState> = _uiState.asStateFlow()

    init {

        viewModelScope.launch {
            val playerResult =
                playerRepository.fetchPlayer(
                    savedStateHandle.get<Team>("team")?.id!!,
                    savedStateHandle.get<Int>("position")!!
                )
            _uiState.update {
                when (playerResult) {
                    is Loading -> {
                        it.copy(loading = true)
                    }
                    is Error -> {
                        it.copy(loading = false, error = playerResult.exception?.message)
                    }
                    is Success -> {
                        it.copy(loading = false, player = playerResult.data)
                    }
                }
            }
        }
    }
}

data class PlayerUiState(
    val loading: Boolean = false,
    val player: Player? = null,
    val error: String? = null
)
