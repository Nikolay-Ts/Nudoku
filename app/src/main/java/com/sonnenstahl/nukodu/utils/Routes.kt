package com.sonnenstahl.nukodu.utils


sealed class Routes(val route: String) {
    data object Home : Routes("home")
    data object Game : Routes("game")
    data object EndScreen : Routes("endScreen")
    data object Profile : Routes("profile")


}