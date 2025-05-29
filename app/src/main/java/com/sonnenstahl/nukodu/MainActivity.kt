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
import com.sonnenstahl.nukodu.utils.Routes
import androidx.compose.material3.Surface
import androidx.navigation.compose.*
import com.sonnenstahl.nukodu.utils.CURRENT_GAME_FN
import com.sonnenstahl.nukodu.utils.Difficulty
import com.sonnenstahl.nukodu.utils.GameState

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
        composable(Routes.Home.route) {
            // pass the navController
            Home(navController = navController, context = context)
        }

        composable("${Routes.Game.route}/{isCurrentGame}/{difficulty}") { backStackEntry ->
            val isCurrentGameStr = backStackEntry.arguments?.getString("isCurrentGame")
            val isCurrentGame = isCurrentGameStr?.toBooleanStrictOrNull() ?: false
            val difficultyStr = backStackEntry.arguments?.getString("difficulty")
            val difficulty = Difficulty.valueOf(difficultyStr ?: Difficulty.EASY.name)
            NudokuScreen(navController, isCurrentGame, difficulty)
        }

        composable("${Routes.EndScreen.route}/{gameState}/{difficulty}") { backStackEntry ->
            val gameStateStr = backStackEntry.arguments?.getString("gameState")
            val gameState = GameState.valueOf(gameStateStr ?: GameState.LOST.name)
            val difficultyStr = backStackEntry.arguments?.getString("difficulty")
            val difficulty = Difficulty.valueOf(difficultyStr ?: Difficulty.EASY.name)
            EndScreen(navController, gameState, difficulty)
        }

        composable(Routes.Profile.route) {
            ProfileScreen()
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