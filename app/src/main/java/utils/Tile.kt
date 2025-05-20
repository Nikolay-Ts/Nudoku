package com.sonnenstahl.nukodu.utils

data class Tile(
    var number: Int = 0,
    var cell: Pair<Int, Int> = Pair(-1,-1),
    var highlight: Boolean = false,
    var isCompleted: Boolean = false,
)