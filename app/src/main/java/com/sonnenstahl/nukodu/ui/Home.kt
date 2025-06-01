package com.sonnenstahl.nukodu.ui

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController
import com.sonnenstahl.nukodu.utils.CURRENT_GAME_FN
import com.sonnenstahl.nukodu.utils.Game
import com.sonnenstahl.nukodu.utils.Routes
import com.sonnenstahl.nukodu.utils.USER_FN
import com.sonnenstahl.nukodu.utils.User
import com.sonnenstahl.nukodu.utils.loadGame
import com.sonnenstahl.nukodu.utils.loadUser
import java.util.Locale

@Composable
fun Home(navController: NavHostController, context: Context) {
    var currentGame by remember { mutableStateOf<Game?>(null) }
    var user by remember { mutableStateOf<User?>( null) }

    // this is to prevent a race condition as otherwise, when quiting
    // the file here is read faster than NudokuView can delete it
    LaunchedEffect(Unit) {
        currentGame = loadGame(context = context, filename = CURRENT_GAME_FN)
        user = loadUser(context = context, filename = USER_FN)
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

        NudokuLogo(modifier = Modifier.padding(20.dp))

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 32.dp)
        ) {
            Column(
                modifier = Modifier.align(Alignment.BottomCenter),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val sharedModifier = Modifier
                    .width(300.dp)
                    .height(60.dp)

                Spacer(modifier = Modifier.height(20.dp))

                if (isCurrentGame) {


                    OutlinedButton(
                        modifier = sharedModifier,
                        onClick = {
                            val difficulty = currentGame?.difficulty?.name
                            navController.navigate("${Routes.Game.route}/true/${difficulty}")
                        }) {
                        val minutes = (currentGame!!.time % 3600) / 60
                        val seconds = currentGame!!.time % 60
                        val timeString = String.format("%02d:%02d", minutes, seconds)
                        val currentDifficulty = currentGame?.difficulty.toString()
                            .lowercase(Locale.getDefault())
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Continue Game\n$currentDifficulty - $timeString",
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }

                Button(
                    modifier = sharedModifier.padding(top = 5.dp),
                    onClick = { showSheet = true }) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "New Game",
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
        }
    }
}