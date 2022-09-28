package com.zgsbrgr.demo.fiba.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.zgsbrgr.demo.fiba.domain.Match
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MatchDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(MatchDetailUIState(loading = true))
    val uiState: StateFlow<MatchDetailUIState> = _uiState.asStateFlow()

    init {

        _uiState.update {
            it.copy(
                loading = false,
                data = savedStateHandle.get<Match>("match")
            )
        }
    }
}

data class MatchDetailUIState(
    val loading: Boolean = false,
    val data: Match? = null,
    val error: String? = null
)
