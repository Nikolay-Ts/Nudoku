package com.sonnenstahl.nukodu.utils

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class User (
    var easy: GameData,
    var medium: GameData,
    var hard: GameData,
    var expert: GameData,
    val gameCompletionDates: MutableList<LocalDate> = mutableListOf()
)


@Serializable
data class GameData(
    var wins: Int,
    var tries: Int,
    var bestTime: Int = 0,
    var worstTime: Int = 0,
    var avgTime: Int = 0
)