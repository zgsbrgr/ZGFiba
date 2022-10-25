package com.zgsbrgr.demo.fiba.ui.matches

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zgsbrgr.demo.fiba.Result
import com.zgsbrgr.demo.fiba.asResult
import com.zgsbrgr.demo.fiba.data.HomeRepository
import com.zgsbrgr.demo.fiba.domain.Match
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MatchesViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: HomeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MatchesUIState(loading = true))
    val uiState: StateFlow<MatchesUIState> = _uiState.asStateFlow()

    init {
        val result = repository.loadDataForHome().asResult()
        result.onEach { fetchResult ->
            _uiState.update {
                when (fetchResult) {
                    is Result.Loading -> {
                        it.copy(loading = true)
                    }
                    is Result.Success -> {
                        it.copy(
                            loading = false,
                            data = fetchResult.data[savedStateHandle.get<Int>("position")!!].matches
                        )
                    }
                    is Result.Error -> {
                        it.copy(
                            loading = false,
                            error = fetchResult.exception?.message.toString()
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }
}
data class MatchesUIState(
    val loading: Boolean = false,
    val data: List<Match> = emptyList(),
    val error: String? = null
)
