package com.zgsbrgr.demo.fiba.domain

import java.util.UUID

data class Section(
    val id: String = UUID.randomUUID().toString(),
    val section: String,
    val matches: List<Match>
)
