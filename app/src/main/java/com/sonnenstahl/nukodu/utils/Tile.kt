package com.sonnenstahl.nukodu.utils

import kotlinx.serialization.Serializable

@Serializable
data class Tile(
    var number: Int = 0,
    var cell: Pos = Pos(-1,-1),
    var highlight: Boolean = false,
    var isCompleted: Boolean = false,
)

@Serializable
data class Pos(var first: Int, var second: Int)
