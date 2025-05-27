package com.sonnenstahl.nukodu

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController
import com.sonnenstahl.nukodu.utils.CURRENT_GAME_FN
import com.sonnenstahl.nukodu.utils.Game
import com.sonnenstahl.nukodu.utils.Routes
import com.sonnenstahl.nukodu.utils.loadGame

@Composable
fun Home(navController: NavHostController, context: Context) {
    val currentGame: Game? = loadGame(context = context, filename = CURRENT_GAME_FN)
    val isCurrentGame = currentGame != null

    Log.d("DATA meow", "$currentGame")
    Log.d("is Saved", "${context.fileList().contains(CURRENT_GAME_FN)}")


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White), contentAlignment = Alignment.Center
    ) {
        Column {

            Spacer(modifier = Modifier.height(20.dp))

            // navigate to Profile Screen
            Button(onClick = {
                navController.navigate("${Routes.Game.route}/false")
            }) {
                Text("New Game")
            }

            if (isCurrentGame) {
                Button(onClick = {
                    navController.navigate("${Routes.Game.route}/true")
                }) {
                    Text("Continue Game")
                }
            }
        }
    }
}