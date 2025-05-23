package com.sonnenstahl.nukodu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.sonnenstahl.nukodu.ui.theme.NukoduTheme
import utils.Routes
import androidx.compose.material3.Surface
import androidx.navigation.compose.*
import utils.GameState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NukoduTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    val context = LocalContext.current

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.Home.route) {
        // Home
        composable(Routes.Home.route) {
            // pass the navController
            Router(navController = navController)
        }
        // Profile
        composable(Routes.Game.route) {
            NudokuScreen(navController)
        }

        composable("${Routes.EndScreen.route}/{gameState}") { backStackEntry ->
            val gameStateStr = backStackEntry.arguments?.getString("gameState")
            val gameState = GameState.valueOf(gameStateStr ?: GameState.LOST.name)
            EndScreen(gameState)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    NukoduTheme {
        MainScreen()
    }
}