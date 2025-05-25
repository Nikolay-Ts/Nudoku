package utils

import android.util.Log
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateMap
import com.sonnenstahl.nukodu.utils.Tile
import com.sonnenstahl.nukodu.utils.validateTile


/**
 * This function is to place a number inside
 * You call it when tapping a cell or a number button
 * Checks if a cell is selected. Also after placing a number
 * it adds to error if it was wrong and also checks if you have won or lost
 */
fun placeNumber(
    i: Int,
    j: Int,
    number: Int,
    nudokuGrid: Array<Array<Tile>>,
    numbersLeft: SnapshotStateMap<Int, Int>,
    numbersDissapear: SnapshotStateMap<Int, Boolean>,
    errors: MutableIntState,
    gameState: MutableState<GameState>,
    onComplete: () -> Unit
) {
    if (number !in 1..9) return

    if (nudokuGrid[i][j].isCompleted || numbersLeft[number]!! <= 0) return

    val cellTile = nudokuGrid[i][j]
    val currentCellNumber = cellTile.number

    cellTile.number = number

    if (numbersLeft[currentCellNumber] != null) {
        numbersLeft[currentCellNumber] = numbersLeft[currentCellNumber]!! + 1
        if (numbersLeft[currentCellNumber] == 1) {
            numbersDissapear[currentCellNumber] = false
        }
    }

    cellTile.isCompleted = validateTile(nudokuGrid, i, j, number)
    numbersLeft[number] = numbersLeft[number]!! - 1

    if (numbersLeft[number]!! == 0 && !cellTile.isCompleted) {
        numbersDissapear[cellTile.number] = true
    }

    if (numbersLeft[number]!! == 0 && cellTile.isCompleted) {
        numbersDissapear[cellTile.number] = false
    }

    if (!cellTile.isCompleted) {
        errors.intValue++
    }

    gameStateChecker(numbersLeft, errors, gameState)

    Log.d("GameState", "$gameState")
    Log.d("Validation", "cell: $i, $j\n is valid?:${cellTile.isCompleted}")

    onComplete()
}