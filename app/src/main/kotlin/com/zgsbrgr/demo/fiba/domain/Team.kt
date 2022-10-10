package com.zgsbrgr.demo.fiba.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Team(
    val id: String,
    val team: String,
    val points: Int,
    val winner: Boolean
) : Parcelable
