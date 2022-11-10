package com.zgsbrgr.demo.fiba

import android.app.Application
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltAndroidApp
class ZGFibaApp : Application()
