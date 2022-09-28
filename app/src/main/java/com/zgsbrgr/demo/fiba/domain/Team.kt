package com.zgsbrgr.demo.fiba.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class Team(
    val id: String = UUID.randomUUID().toString(),
    val team: String,
    val points: Int,
    val winner: Boolean
) : Parcelable
