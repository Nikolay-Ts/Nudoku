package com.sonnenstahl.nukodu.utils

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateMap

/**
 * Places a number in the Sudoku grid if the move is valid.
 * Checks whether the inserted number matches the original solution.
 * Handles vibration feedback, error tracking, and win/loss checking.
 */
fun placeNumber(
    i: Int,
    j: Int,
    number: Int,
    nudokuGrid: Array<Array<Tile>>,
    originalGrid: Array<Array<Tile>>,
    numbersLeft: SnapshotStateMap<Int, Int>,
    errors: MutableIntState,
    gameState: MutableState<GameState>,
    context: Context,
    onComplete: () -> Unit
) {
    if (number !in 1..9) return
    val tile = nudokuGrid[i][j]

    if (tile.isCompleted || numbersLeft[number]!! <= 0) return
    val previousNumber = tile.number

    if (previousNumber in 1..9) {
        numbersLeft[previousNumber] = numbersLeft[previousNumber]!! + 1
    }

    tile.number = number

    val correctNumber = originalGrid[i][j].number
    tile.isCompleted = number == correctNumber

    numbersLeft[number] = numbersLeft[number]!! - 1

    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if (tile.isCompleted) {
        vibrateOnce(vibrator, 50)
    } else {
        vibrateOnce(vibrator, 500)
        errors.intValue++
    }

    gameStateChecker(numbersLeft, errors, gameState)

    onComplete()
}

/**
 * Vibrates the device for a given duration in ms.
 */
private fun vibrateOnce(vibrator: Vibrator, duration: Long) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibrator.vibrate(
            VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE)
        )
    } else {
        @Suppress("DEPRECATION")
        vibrator.vibrate(duration)
    }
}