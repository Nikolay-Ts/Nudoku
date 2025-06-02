package com.sonnenstahl.nukodu.utils

import android.content.Context
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateMap

/**
 * This takes all of the features form the loaded game from disk and updates
 * all of the snapshot variables to be rendered. The numbers left are not saved
 * as you can deduce how many you have left by subtracting each time.
 */
fun importLoadedGame(
    game: Game,
    nudokuGrid: Array<Array<Tile>>,
    originalGrid: Array<Array<Tile>>,
    numbersLeft: SnapshotStateMap<Int, Int>,
    errors: MutableIntState,
    gameState: MutableState<GameState>,
    gameTimeSeconds: MutableIntState
) {
    errors.intValue = game.errors
    gameState.value = game.gameState
    gameTimeSeconds.intValue = game.time

    for (row in 0..8) {
        for (col in 0..8) {
            nudokuGrid[row][col] = game.nudokuGrid[row][col]
            originalGrid[row][col] = game.originalGrid[row][col]
            val number = nudokuGrid[row][col].number

            if (number == 0) {
                continue
            }

            numbersLeft[number] =  numbersLeft[number]!! - 1
        }
    }
}

/**
 * This is meant to be called every time you want to
 * save the current gameState to disk. Should be used
 * with some mutexes if you are saving time, score, error
 * asynchronously
 */
fun updateAndSave(
    difficulty: Difficulty,
    nudokuGrid: Array<Array<Tile>>,
    originalGrid: Array<Array<Tile>>,
    errors: Int,
    gameState: GameState,
    time: Int,
    context: Context
) {
    val newGame = Game(
        difficulty = difficulty,
        nudokuGrid = nudokuGrid,
        originalGrid = originalGrid,
        errors = errors,
        gameState = gameState,
        time = time
    )
    saveGame(context = context, currentGame = newGame, filename = CURRENT_GAME_FN)
}