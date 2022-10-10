package com.zgsbrgr.demo.fiba.domain

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
