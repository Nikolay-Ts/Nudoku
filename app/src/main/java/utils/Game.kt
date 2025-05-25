package utils

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
    var NudokuGrid: Array<Array<Tile>>,
    var errors: Int,
    var gamState: GameState,
    var time: Int,
)