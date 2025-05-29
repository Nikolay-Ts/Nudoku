package com.sonnenstahl.nukodu

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sonnenstahl.nukodu.utils.CURRENT_GAME_FN
import com.sonnenstahl.nukodu.utils.Difficulty
import com.sonnenstahl.nukodu.utils.GameState
import com.sonnenstahl.nukodu.utils.Routes
import com.sonnenstahl.nukodu.utils.USER_FN
import com.sonnenstahl.nukodu.utils.deleteFile
import com.sonnenstahl.nukodu.utils.loadUser
import com.sonnenstahl.nukodu.utils.saveUser

@Composable
fun EndScreen(navController: NavController, gameState: GameState, difficulty: Difficulty) {
    BackHandler { /*PREVENTS GOING BACK TO GAME*/}

    var showSheet by remember { mutableStateOf(false) }
    val context = LocalContext.current
    deleteFile(context = context, filename = CURRENT_GAME_FN)

    if (gameState == GameState.WON) {
        updateUserWins(context = context, difficulty = difficulty, USER_FN)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Center content
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val message = when (gameState) {
                GameState.WON -> "🎉 You Won!"
                GameState.LOST -> "❌ Game Over"
                else -> "Unknown State"
            }

            Text(text = message)
            Spacer(modifier = Modifier.padding(vertical = 50.dp))
        }

        // Bottom-aligned buttons
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = { navController.navigate(Routes.Home.route) }) {
                Text("Home")
            }

            Button(onClick = { showSheet = true }) {
                Text("Play Again")
            }
        }

        GameModeBottomSheet(
            showSheet = showSheet,
            onDismiss = { showSheet = false },
            navController = navController
        )
    }
}

fun updateUserWins(context: Context, difficulty: Difficulty, filename: String? = USER_FN) {
    // this should not happen, as we have already inited a User when creating the game
    val user = loadUser(context = context, filename = filename) ?: return

    when (difficulty) {
        Difficulty.EASY -> user.easyWins++
        Difficulty.MEDIUM -> user.mediumWins++
        Difficulty.HARD -> user.hardWins++
        Difficulty.EXPERT -> user.expertWins++
    }

    saveUser(context = context, user = user)
}
