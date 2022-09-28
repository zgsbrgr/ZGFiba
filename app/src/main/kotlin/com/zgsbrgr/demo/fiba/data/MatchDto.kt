package com.zgsbrgr.demo.fiba.data

import com.zgsbrgr.demo.fiba.domain.Match
import com.zgsbrgr.demo.fiba.domain.Team
import kotlinx.serialization.Serializable

@Serializable
data class MatchDto(
    val date: String,
    val tag: String,
    val thumb: String?,
    val home: TeamDto,
    val away: TeamDto
)

@Serializable
data class TeamDto(
    val team: String,
    val points: Int,
    val winner: Boolean
)

fun MatchDto.toDomain(): Match = Match(
    date = this.date,
    tag = this.tag,
    thumb = this.thumb,
    home = this.home.toDomain(),
    away = this.away.toDomain()
)

fun TeamDto.toDomain(): Team = Team(
    team = this.team,
    points = this.points,
    winner = this.winner
)
