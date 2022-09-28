package com.zgsbrgr.demo.fiba.domain

import java.util.UUID

data class Team(
    val id: String = UUID.randomUUID().toString(),
    val team: String,
    val points: Int,
    val winner: Boolean
)
