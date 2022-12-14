@file:Suppress("MagicNumber")
package com.zgsbrgr.demo.fiba.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zgsbrgr.demo.fiba.Result
import com.zgsbrgr.demo.fiba.asResult
import com.zgsbrgr.demo.fiba.data.GameInfoRepository
import com.zgsbrgr.demo.fiba.domain.MatchEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import javax.inject.Inject

const val ARG_OBJECT = "arg_object"
const val ARG_MATCH_ID = "arg_match_id"

@HiltViewModel
class GameInfoViewModel @Inject constructor(
    private val gameInfoRepository: GameInfoRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableSharedFlow<EventUIState>(
        extraBufferCapacity = 1,
        onBufferOverflow =
        BufferOverflow.SUSPEND
    )
    val uiState: SharedFlow<EventUIState> = _uiState.asSharedFlow()

    private var job = Job()
        get() {
            if (field.isCancelled) field = Job()
            return field
        }

    val scope = CoroutineScope(job + Dispatchers.Main)

    val uiStateWithShareIn: SharedFlow<EventUIState> =
        gameInfoRepository.fetchGameInfo(savedStateHandle.get<String>(ARG_MATCH_ID)!!)
            .asResult().map { result ->
                when (result) {
                    is Result.Loading -> {
                        EventUIState(loading = true)
                    }
                    is Result.Success -> {
                        EventUIState(loading = false, data = result.data)
                    }
                    is Result.Error -> {
                        EventUIState(loading = false, error = result.exception?.message)
                    }
                }
            }.shareIn(
                scope = scope,
                started = SharingStarted.WhileSubscribed(5_000),
                replay = 1
            )

    fun loadEvents() {

        if (savedStateHandle.get<Int>(ARG_OBJECT) == 1) {
            val infoResult =
                gameInfoRepository.fetchGameInfo(savedStateHandle.get<String>(ARG_MATCH_ID)!!)
                    .asResult()

            viewModelScope.launch(job) {
                infoResult.map { result ->
                    when (result) {
                        is Result.Loading -> {
                            EventUIState(loading = true)
                        }
                        is Result.Success -> {
                            EventUIState(loading = false, data = result.data)
                        }
                        is Result.Error -> {
                            EventUIState(loading = false, error = result.exception?.message)
                        }
                    }
                }.collect { eventUiState ->
                    _uiState.tryEmit(
                        eventUiState
                    )
                }
            }
        }
    }

    fun removeUpdates() {
        job.cancel()
    }
}

data class EventUIState(
    val loading: Boolean = false,
    val data: MatchEvent? = null,
    val error: String? = null
)
