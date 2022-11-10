package com.zgsbrgr.demo.fiba.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.zgsbrgr.demo.fiba.Result
import com.zgsbrgr.demo.fiba.asResult
import com.zgsbrgr.demo.fiba.data.GameInfoRepository
import com.zgsbrgr.demo.fiba.domain.MatchEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import java.lang.IllegalArgumentException

const val ARG_OBJECT = "arg_object"
const val ARG_MATCH_ID = "arg_match_id"

@HiltViewModel
class GameInfoViewModel @Inject constructor(
    gameInfoRepository: GameInfoRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(EventUIState(loading = true))
    val uiState: StateFlow<EventUIState> = _uiState.asStateFlow()

    init {
        // limit only to summary tab
        if (savedStateHandle.get<Int>(ARG_OBJECT) == 1) {
            val infoResult =
                gameInfoRepository.fetchGameInfo(savedStateHandle.get<String>(ARG_MATCH_ID)!!).asResult()
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
    }

}

data class EventUIState(
    val loading: Boolean = false,
    val data: MatchEvent? = null,
    val error: String? = null
)
