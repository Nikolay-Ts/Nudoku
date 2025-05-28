package com.sonnenstahl.nukodu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.sonnenstahl.nukodu.utils.GameState

@Composable
fun EndScreen(gameState: GameState) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            val message = when (gameState) {
                GameState.WON -> "🎉 You Won!"
                GameState.LOST -> "❌ Game Over"
                else -> "Unknown State"
            }

            Text(text = message)
        }
    }
}
