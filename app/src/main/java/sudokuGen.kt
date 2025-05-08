package com.sonnenstahl.nukodu

import kotlin.random.Random

private fun unUsedInBox(
    grid: Array<IntArray>,
    rowStart: Int,
    colStart: Int,
    number: Int
): Boolean {
    for (i in 0..<3) {
        for (j in 0..<3) {
            if (grid[rowStart + i][colStart + j] == number) {
                return false
            }
        }
    }
    return true
}

private fun fillBox(
    grid: Array<IntArray>,
    row: Int,
    col: Int
) {
    for (i in 0..<3) {
        for (j in 0..<3) {
            while (true) {
                val number = Random.nextInt(1, 10)
                if (unUsedInBox(grid, row, col, number)) {
                    grid[row + i][col + j] = number
                    break
                }
            }
        }
    }
}

private fun unUsedInRow(grid: Array<IntArray>, i: Int, number: Int): Boolean {
    return number !in grid[i]
}

private fun unUsedInCol(grid: Array<IntArray>, j: Int, number: Int): Boolean {
    for (i in 0..<9) {
        if (grid[i][j] == number) {
            return false
        }
    }
    return true
}

private fun isSafe(grid: Array<IntArray>, i: Int, j: Int, number: Int): Boolean {
    return unUsedInRow(grid, i, number) &&
            unUsedInCol(grid, j, number) &&
            unUsedInBox(grid, i - i % 3, j - j % 3, number)
}

private fun fillDiagonal(grid: Array<IntArray>) {
    for (i in 0..<9 step 3) {
        fillBox(grid, i, i)
    }
}

private fun fillRemaining(grid: Array<IntArray>, i: Int, j: Int): Boolean {
    if (i == 9) return true

    var row = i
    var col = j

    if (col == 9) {
        row++
        col = 0
    }

    if (row == 9) return true

    if (grid[row][col] != 0) {
        return fillRemaining(grid, row, col + 1)
    }

    for (number in 1..9) {
        if (isSafe(grid, row, col, number)) {
            grid[row][col] = number
            if (fillRemaining(grid, row, col + 1)) return true
            grid[row][col] = 0
        }
    }
    return false
}

private fun removeNumbers(grid: Array<IntArray>, k: Int) {
    var tempk = k
    while (tempk > 0) {
        val cellId = Random.nextInt(0, 81)

        val i = cellId / 9
        val j = cellId % 9

        if (grid[i][j] != 0) {
            grid[i][j] = 0
            tempk--
        }
    }
}

/**
 * This function takes in the 9x9 array and generates
 * a new sudoku pattern with k elements missing in it
 * this should only be called once or will crash the app
 */
fun createNudoku(grid: Array<IntArray>, k: Int) {
    fillDiagonal(grid)
    fillRemaining(grid, 0, 0)
    removeNumbers(grid, k)
}