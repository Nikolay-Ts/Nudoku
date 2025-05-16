package com.sonnenstahl.nukodu.utils

import androidx.compose.runtime.snapshots.SnapshotStateMap

/*
 * The point of this function is to make sure that the user
 * does not chose a number that been used 9 times
*/
fun checkSelected(
    numbersLeft: SnapshotStateMap<Int, Int>,
    currentlySelected: Int,
    newSelected: Int
): Int {
    return if (numbersLeft[currentlySelected] == 0) {
        currentlySelected
    } else {
        newSelected
    }
}

// TODO: make a new fun that does not allow add the addition to a grid if there are no more numbers

