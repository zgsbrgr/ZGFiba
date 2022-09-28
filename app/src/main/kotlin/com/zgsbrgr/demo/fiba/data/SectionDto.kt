package com.zgsbrgr.demo.fiba.data

import com.zgsbrgr.demo.fiba.domain.Section
import kotlinx.serialization.Serializable

@Serializable
data class SectionDto(
    val section: String,
    val matches: List<MatchDto>
)

fun SectionDto.toDomain(): Section = Section(
    section = this.section,
    matches = this.matches.map {
        it.toDomain()
    }
)
