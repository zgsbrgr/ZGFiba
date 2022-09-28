package com.zgsbrgr.demo.fiba.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class Match(
    val id: String = UUID.randomUUID().toString(),
    val date: String,
    val tag: String,
    val thumb: String?,
    val home: Team,
    val away: Team
) : Parcelable
