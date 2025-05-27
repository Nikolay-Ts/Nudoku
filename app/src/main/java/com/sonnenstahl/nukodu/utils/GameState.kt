package com.sonnenstahl.nukodu.utils

import kotlinx.serialization.Serializable

@Serializable
enum class GameState {
    RUNNING,
    PAUSED,
    WON,
    LOST
}