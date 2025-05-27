package com.sonnenstahl.nukodu.utils

import kotlinx.serialization.Serializable

@Serializable
enum class Difficulty {
    EASY,
    MEDIUM,
    HARD,
    EXPERT
}



@Serializable
data class Game (
    val difficulty: Difficulty,
    var nudokuGrid: Array<Array<Tile>>,
    var errors: Int,
    var gameState: GameState,
    var time: Int,
)