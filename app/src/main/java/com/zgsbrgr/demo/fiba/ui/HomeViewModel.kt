package com.zgsbrgr.demo.fiba.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zgsbrgr.demo.fiba.Result
import com.zgsbrgr.demo.fiba.asResult
import com.zgsbrgr.demo.fiba.data.HomeRepository
import com.zgsbrgr.demo.fiba.domain.Section
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(HomeUIState<Section>(loading = true))
    val uiState: StateFlow<HomeUIState<Section>> = _uiState.asStateFlow()

    init {
        val result = homeRepository.loadDataForHome().asResult()
        result.onEach { dataResult->
            _uiState.update {
                when(dataResult) {
                    is Result.Loading -> {
                        it.copy(loading = true)
                    }
                    is Result.Error -> {
                        it.copy(loading = false, errorMessage = dataResult.exception?.message)
                    }
                    is Result.Success -> {
                        it.copy(loading = false, data = dataResult.data)
                    }
                }
            }
        }.launchIn(viewModelScope)

    }

}

data class HomeUIState<T>(
    val loading: Boolean = false,
    val data: List<T> = emptyList(),
    val errorMessage: String? = null
)