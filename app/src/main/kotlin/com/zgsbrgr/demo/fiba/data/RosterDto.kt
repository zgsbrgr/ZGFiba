package com.zgsbrgr.demo.fiba.data

import com.zgsbrgr.demo.fiba.domain.Player
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RosterDto(
    @SerialName("Player") val player: String,
    @SerialName("Pos.") val pos: String,
    @SerialName("Height") val height: String,
    @SerialName("Age") val age: Int?,
    @SerialName("Pts") val pts: Double,
    @SerialName("Reb") val reb: Double,
    @SerialName("Ast") val ast: Double,
    @SerialName("GP") val gp: Int,
    @SerialName("W-L") val winLose: String,
    @SerialName("MIN") val min: Double,
    @SerialName("2P%") val twoPercent: String,
    @SerialName("3P%") val threePercent: String,
    @SerialName("FG%") val fieldGoalPercent: String,
    @SerialName("FT%") val freeThrowPercent: String,
    @SerialName("Or") val offReb: Double,
    @SerialName("Reb__1") val reb1: Double,
    @SerialName("Ast__1") val ast1: Double,
    @SerialName("Stl") val steal: Double,
    @SerialName("To") val to: Double,
    @SerialName("Blk") val block: Double,
    @SerialName("Fo") val fo: Double,
    @SerialName("Pts__1") val pts1: Double,
    @SerialName("Eff") val eff: Double
)

fun RosterDto.toRoster(): Player =
    Player(
        player = this.player,
        playedPosition = this.pos,
        height = this.height,
        age = this.age,
        pointsAvg = this.pts,
        reboundsAvg = this.reb,
        assistsAvg = this.ast,
        gamesPlayed = this.gp,
        winLose = this.winLose,
        minutesPlayed = this.min,
        twoPointsPercentage = this.twoPercent,
        threePointsPercentage = this.threePercent,
        fieldGoalsPercentage = this.fieldGoalPercent,
        freeThrowPercentage = this.freeThrowPercent,
        offensiveRebound = this.offReb,
        steals = this.steal,
        turnOvers = this.to,
        blocks = this.block,
        fouls = this.fo,
        efficiency = this.eff
    )
