package com.sonnenstahl.nukodu.utils

import android.util.Log
import androidx.compose.runtime.snapshots.SnapshotStateMap
import com.sonnenstahl.nukodu.utils.Pos
import kotlin.random.Random
import com.sonnenstahl.nukodu.utils.Tile

private fun unUsedInBox(
    grid: Array<Array<Tile>>,
    rowStart: Int,
    colStart: Int,
    number: Int
): Boolean {
    for (i in 0..<3) {
        for (j in 0..<3) {
            if ((grid[rowStart + i][colStart + j]).number == number) {
                return false
            }
        }
    }
    return true
}

private fun fillBox(
    grid: Array<Array<Tile>>,
    numbersLeft: SnapshotStateMap<Int, Int>,
    row: Int,
    col: Int
) {
    for (i in 0..<3) {
        for (j in 0..<3) {
            while (true) {
                val number = Random.nextInt(1, 10)
                val tile = grid[row + i][col + j]
                if (unUsedInBox(grid, row, col, number)) {
                    tile.number = number
                    tile.cell = Pos(i, j)
                    tile.isCompleted = true
                    numbersLeft[number] = numbersLeft[number]!! - 1
                    break
                }
            }
        }
    }
}

//    return number !in grid[i] (same logic)
private fun unUsedInRow(grid: Array<Array<Tile>>, i: Int, number: Int): Boolean {
    for (rowTile in grid[i]) {
        if (number == rowTile.number) {
            return false
        }
    }
    return true
}

private fun unUsedInCol(grid: Array<Array<Tile>>, j: Int, number: Int): Boolean {
    for (i in 0..<9) {
        if (grid[i][j].number == number) {
            return false
        }
    }
    return true
}

fun isSafe(grid: Array<Array<Tile>>, i: Int, j: Int, number: Int): Boolean {
    return unUsedInRow(grid, i, number) &&
            unUsedInCol(grid, j, number) &&
            unUsedInBox(grid, i - i % 3, j - j % 3, number)
}

private fun fillDiagonal(grid: Array<Array<Tile>>, numbersLeft: SnapshotStateMap<Int, Int>) {
    for (i in 0..<9 step 3) {
        fillBox(grid, numbersLeft, i, i)
    }
}

private fun fillRemaining(
    grid: Array<Array<Tile>>,
    numbersLeft: SnapshotStateMap<Int, Int>,
    i: Int,
    j: Int
): Boolean {
    if (i == 9) return true

    var row = i
    var col = j

    if (col == 9) {
        row++
        col = 0
    }

    if (row == 9) return true

    if (grid[row][col].number != 0) {
        return fillRemaining(grid, numbersLeft, row, col + 1)
    }

    for (number in 1..9) {
        if (isSafe(grid, row, col, number)) {
            val tile = grid[row][col]

            tile.number = number
            tile.cell = Pos(row, col)
            tile.isCompleted = true

            numbersLeft[number] = numbersLeft[number]!! - 1

            if (fillRemaining(grid, numbersLeft, row, col + 1)) return true

            Log.d("Triggered in Fill Remain", "$numbersLeft" )
            tile.number = 0
            tile.isCompleted = false
            numbersLeft[number] = numbersLeft[number]!! + 1
        }
    }
    return false
}

// TODO: make a pseudo better random removal generator to balance out the removals

private fun removeNumbers(
    grid: Array<Array<Tile>>,
    numbersLeft: SnapshotStateMap<Int, Int>,
    k: Int
) {
    var tempk = k
    while (tempk > 0) {
        val cellId = Random.nextInt(0, 81)

        val i = cellId / 9
        val j = cellId % 9
        val tile = grid[i][j]

        if (tile.number != 0) {
            numbersLeft[tile.number] = numbersLeft[tile.number]!! + 1
            tile.number = 0
            tile.isCompleted = false
            tempk--
        }
    }
}


/**
 * This function takes in the 9x9 array and generates
 * a new sudoku pattern with k elements missing in it
 * this should only be called once or will crash the app
 */
fun createNudoku(
    grid: Array<Array<Tile>>,
    numbersLeft: SnapshotStateMap<Int, Int>,
    k: Int
): Array<Array<Tile>> {
    fillDiagonal(grid, numbersLeft)
    fillRemaining(grid, numbersLeft,  0, 0)

    val originalGrid = cloneGrid(grid)

    removeNumbers(grid, numbersLeft, k)

    return  originalGrid
}

/**
 * just so that we can have an og grid and just compare current index,
 * makes comparison constant time
 */
fun cloneGrid(grid: Array<Array<Tile>>): Array<Array<Tile>> {
    return Array(9) { i ->
        Array(9) { j ->
            val original = grid[i][j]
            Tile(
                number = original.number,
                cell = original.cell.copy(),
                isCompleted = original.isCompleted
            )
        }
    }
}
