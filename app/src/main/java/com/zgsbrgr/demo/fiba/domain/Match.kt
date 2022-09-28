package com.zgsbrgr.demo.fiba.domain

import java.util.*

data class Match(
    val id: String = UUID.randomUUID().toString(),
    val date: String,
    val tag: String,
    val thumb: String?,
    val home: Team,
    val away: Team
)

