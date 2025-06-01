package com.sonnenstahl.nukodu.ui

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
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
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * Shows user stats fot the current game if they won
 * Allows user to check their overall stats, play again or go back to Home
 */
@Composable
fun EndScreen(
    navController: NavController,
    gameState: GameState,
    difficulty: Difficulty,
    time: Int,
    errors: Int
) {
    BackHandler(enabled = false) { /*PREVENT GOING BACK*/}

    var showSheet by remember { mutableStateOf(false) }
    val isBestTime = remember { mutableStateOf(false) }
    val context = LocalContext.current

    deleteFile(context = context, filename = CURRENT_GAME_FN)

    if (gameState == GameState.WON) {
        updateUserWins(context, difficulty, isBestTime, time, USER_FN)
    }

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

        // Center content
        Column(
            modifier = Modifier.align(Alignment.Center).padding(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            if (gameState == GameState.WON) {
                Text(
                    "🎉 You Won!",
                    modifier = Modifier.padding(vertical = 50.dp)
                )
                val minutes = (time % 3600) / 60
                val seconds = time % 60
                val formattedTime = String.format("%02d:%02d", minutes, seconds)

                if (isBestTime.value) {
                    Text("New Best Time!")

                }

                Text(formattedTime)

                Text("Mistakes", modifier = Modifier.padding(top = 10.dp))
                Text("$errors/3 ")

            } else {
                Text("❌ Game Over")
            }

            Spacer(modifier = Modifier.padding(vertical = 50.dp))
        }

        // Bottom-aligned buttons
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

                OutlinedButton(
                    modifier = sharedModifier,
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

                Button(
                    modifier = sharedModifier.padding(top = 5.dp),
                    onClick = { navController.navigate(Routes.Home.route) }
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Home",
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
        }

        GameModeBottomSheet(
            showSheet = showSheet,
            onDismiss = { showSheet = false },
            navController = navController
        )
    }
}

fun updateUserWins(
    context: Context,
    difficulty: Difficulty,
    isBestTime: MutableState<Boolean>,
    time : Int,
    filename: String? = USER_FN
) {
    // this should not happen, as we have already inited a User when creating the game
    val user = loadUser(context = context, filename = filename) ?: return

    val data = when (difficulty) {
        Difficulty.EASY -> user.easy
        Difficulty.MEDIUM -> user.medium
        Difficulty.HARD -> user.hard
        Difficulty.EXPERT -> user.expert
    }

    data.wins++

    if (data.bestTime == 0 || time < data.bestTime) {
        data.bestTime = time
        isBestTime.value = true
    }

    if (time > data.worstTime) {
        data.worstTime = time
    }

    val totalTime = data.avgTime * (data.wins - 1) + time
    data.avgTime = totalTime / data.wins

    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    user.gameCompletionDates.apply {
        add(today)
    }

    saveUser(context = context, user = user)
}