package com.zgsbrgr.demo.fiba.domain

import com.zgsbrgr.demo.fiba.ui.HomeUIState
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID

data class Section(
    val id: String = UUID.randomUUID().toString(),
    val section: String,
    val matches: List<Match>
)


