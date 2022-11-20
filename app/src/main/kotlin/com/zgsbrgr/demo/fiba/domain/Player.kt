package com.zgsbrgr.demo.fiba.domain

import com.zgsbrgr.demo.fiba.ui.adapter.StatData
import java.util.UUID

data class Player(
    val id: String = UUID.randomUUID().toString(),
    val player: String,
    val playedPosition: String,
    val height: String,
    val age: Int?,
    val pointsAvg: Double,
    val reboundsAvg: Double,
    val assistsAvg: Double,
    val gamesPlayed: Int,
    val winLose: String,
    val minutesPlayed: Double,
    val twoPointsPercentage: String,
    val threePointsPercentage: String,
    val fieldGoalsPercentage: String,
    val freeThrowPercentage: String,
    val offensiveRebound: Double,
    val steals: Double,
    val turnOvers: Double,
    val blocks: Double,
    val fouls: Double,
    val efficiency: Double
)

fun Player.toStringList() = listOf<String>(
    playedPosition,
    height,
    age?.toString() ?: "",
    pointsAvg.toString(),
    reboundsAvg.toString(),
    assistsAvg.toString(),
    gamesPlayed.toString(),
    winLose,
    minutesPlayed.toString(),
    twoPointsPercentage,
    threePointsPercentage,
    fieldGoalsPercentage,
    freeThrowPercentage,
    offensiveRebound.toString(),
    steals.toString(),
    turnOvers.toString(),
    blocks.toString(),
    fouls.toString(),
    efficiency.toString()
)

fun Player.toStatDataStringList() = listOf<String>(
    pointsAvg.toString(),
    reboundsAvg.toString(),
    assistsAvg.toString()
)

fun Player.toStatData() =
    StatData(
        label = this.player,
        items = this.toStatDataStringList()
    )

fun List<Player>.toStatDataList(): List<StatData> {
    return map { item ->
        item.toStatData()
    }
}
