package com.zgsbrgr.demo.fiba.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.zgsbrgr.demo.fiba.Result
import com.zgsbrgr.demo.fiba.asResult
import com.zgsbrgr.demo.fiba.data.GameInfoRepository
import com.zgsbrgr.demo.fiba.domain.MatchEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import java.lang.IllegalArgumentException

class GameInfoViewModel(
    private val gameInfoRepository: GameInfoRepository,
    matchId: String
) : ViewModel() {

    private val _uiState = MutableStateFlow(EventUIState(loading = true))
    val uiState: StateFlow<EventUIState> = _uiState.asStateFlow()

    init {

        val infoResult = gameInfoRepository.fetchGameInfo(matchId).asResult()
        infoResult.onEach { result ->

            _uiState.update {
                when (result) {
                    is Result.Loading -> {
                        it.copy(loading = true)
                    }
                    is Result.Success -> {
                        it.copy(loading = false, data = result.data)
                    }
                    is Result.Error -> {
                        it.copy(loading = false, error = result.exception?.message)
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    @Suppress("UNCHECKED_CAST")
    class GameInfoViewModelFactory(val repository: GameInfoRepository, val arg: String?) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(GameInfoViewModel::class.java)) {
                return GameInfoViewModel(repository, arg!!) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

data class EventUIState(
    val loading: Boolean = false,
    val data: MatchEvent? = null,
    val error: String? = null
)
