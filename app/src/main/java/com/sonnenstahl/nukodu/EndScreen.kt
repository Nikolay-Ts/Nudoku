package com.sonnenstahl.nukodu

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
import com.sonnenstahl.GameModeBottomSheet
import com.sonnenstahl.nukodu.utils.CURRENT_GAME_FN
import com.sonnenstahl.nukodu.utils.GameState
import com.sonnenstahl.nukodu.utils.Routes
import com.sonnenstahl.nukodu.utils.deleteFile

@Composable
fun EndScreen(navController: NavController, gameState: GameState) {
    BackHandler { /*PREVENTS GOING BACK TO GAME*/}

    var showSheet by remember { mutableStateOf(false) }
    val context = LocalContext.current
    deleteFile(context = context, filename = CURRENT_GAME_FN)

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
            Button(onClick = { navController.navigate(Routes.Home) }) {
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