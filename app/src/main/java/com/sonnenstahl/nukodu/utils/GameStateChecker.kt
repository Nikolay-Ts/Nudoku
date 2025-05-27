package com.sonnenstahl.nukodu.utils

import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateMap

// given the amount of numbers used and errors it will set it to WON or LOST
// TODO: Add implementation to pause the game, good for timer
fun gameStateChecker(
    numbersLeft: SnapshotStateMap<Int, Int>,
    errors: MutableIntState,
    gameState: MutableState<GameState>
) {
    if (errors.intValue == 3) {
        gameState.value = GameState.LOST
        return
    }

    if (numbersLeft.all { it.value == 0 }) {
        gameState.value = GameState.WON
        return
    }


}