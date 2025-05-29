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
import com.sonnenstahl.nukodu.utils.Difficulty
import com.sonnenstahl.nukodu.utils.Game
import com.sonnenstahl.nukodu.utils.Routes
import com.sonnenstahl.nukodu.utils.loadGame

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(navController: NavHostController, context: Context) {
    val currentGame: Game? = loadGame(context = context, filename = CURRENT_GAME_FN)
    val isCurrentGame = currentGame != null

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showSheet by remember { mutableStateOf(false) }

    // overlay to display the different dificulty
    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = sheetState
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Text("Choose Game Mode", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = {
                        showSheet = false
                        val difficulty = Difficulty.EASY.name
                        navController.navigate("${Routes.Game.route}/false/$difficulty")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    Text("Easy", color = Color.Black)
                }

                Spacer(Modifier.height(8.dp))

                Button(
                    onClick = {
                        showSheet = false
                        val difficulty = Difficulty.MEDIUM.name
                        navController.navigate("${Routes.Game.route}/false/$difficulty")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    Text("Medium", color = Color.Black)
                }

                Spacer(Modifier.height(8.dp))

                Button(
                    onClick = {
                        showSheet = false
                        val difficulty = Difficulty.HARD.name
                        navController.navigate("${Routes.Game.route}/false/$difficulty")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    Text("Hard", color = Color.Black)
                }

                Button(
                    onClick = {
                        showSheet = false
                        val difficulty = Difficulty.EXPERT.name
                        navController.navigate("${Routes.Game.route}/false/$difficulty")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    Text("Expert", color = Color.Red)
                }

                Spacer(Modifier.height(16.dp))

                TextButton(onClick = { showSheet = false }, modifier = Modifier.fillMaxWidth()) {
                    Text("Cancel", color = Color.Black)
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(20.dp))

            Button(onClick = { showSheet = true }) {
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