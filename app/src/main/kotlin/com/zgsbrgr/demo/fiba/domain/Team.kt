package com.zgsbrgr.demo.fiba.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class Team(
    val id: String,
    val team: String,
    val points: Int,
    val winner: Boolean
) : Parcelable

data class Player(
    val id: String = UUID.randomUUID().toString(),
    val teamId: String,
    val firstName: String,
    val lastName: String,
    val jerseyNumber: Int,
    val height: Int,
    val weight: Int,
    val age: Int
)
