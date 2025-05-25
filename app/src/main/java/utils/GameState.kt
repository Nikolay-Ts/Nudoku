package utils

import kotlinx.serialization.Serializable

@Serializable
enum class GameState {
    RUNNING,
    PAUSED,
    WON,
    LOST
}