@file:Suppress("MagicNumber")
package com.zgsbrgr.demo.fiba

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zgsbrgr.demo.fiba.data.util.NetworkMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MyActivityViewModel @Inject constructor(
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    val isOffline = networkMonitor.isOnline
        .map(Boolean::not)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )

    override fun onCleared() {
        super.onCleared()
        Log.d("connected viewmodel", "onCleared()")
    }
}
