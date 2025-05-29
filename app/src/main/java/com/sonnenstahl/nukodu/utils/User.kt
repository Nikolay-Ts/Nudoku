package com.sonnenstahl.nukodu.utils

import kotlinx.serialization.Serializable

@Serializable
data class User (
    var easyWins: Int,
    var easyTries: Int,
    var mediumWins: Int,
    var mediumTries: Int,
    var hardWins: Int,
    var hardTries: Int,
    var expertWins: Int,
    var expertTries: Int
)