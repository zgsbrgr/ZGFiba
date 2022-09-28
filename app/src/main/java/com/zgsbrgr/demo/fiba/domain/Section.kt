package com.zgsbrgr.demo.fiba.domain

import java.util.*

data class Section(
    val id: String = UUID.randomUUID().toString(),
    val section: String,
    val matches: List<Match>
)
