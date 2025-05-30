package com.sonnenstahl.nukodu

import android.content.Context
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(navController: NavHostController, context: Context) {
    var currentGame by remember { mutableStateOf<Game?>(null) }

    // this is to prevent a race condition as otherwise, when quiting
    // the file here is read faster than NudokuView can delete it
    LaunchedEffect(Unit) {
        currentGame = loadGame(context = context, filename = CURRENT_GAME_FN)
    }

    val isCurrentGame = currentGame != null

    var showSheet by remember { mutableStateOf(false) }

    // overlay to display the different difficulty
    GameModeBottomSheet(showSheet, onDismiss = {showSheet = false}, navController)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Button(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(vertical = 20.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            onClick = { navController.navigate(Routes.Profile.route) }
        ) {
            SvgImageFromAssets("profile-icon.svg", scale = 5.0f)
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(20.dp))

            Button(onClick = { showSheet = true }) {
                Text("New Game")
            }

            if (isCurrentGame) {
                Button(onClick = {
                    val difficulty = currentGame?.difficulty?.name
                    navController.navigate("${Routes.Game.route}/true/${difficulty}")
                }) {
                    Text("Continue Game")
                }
            }
        }
    }
}