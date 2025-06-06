package com.sonnenstahl.nukodu.ui


import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sonnenstahl.nukodu.utils.Difficulty
import com.sonnenstahl.nukodu.utils.GameData
import com.sonnenstahl.nukodu.utils.Routes
import com.sonnenstahl.nukodu.utils.USER_FN
import com.sonnenstahl.nukodu.utils.User
import com.sonnenstahl.nukodu.utils.loadUser
import com.sonnenstahl.nukodu.utils.saveUser

/**
 * Use to overlay all the possible gameModes. Should be used for Home and Endscreen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameModeBottomSheet(
    showSheet: Boolean,
    onDismiss: () -> Unit,
    navController: NavController
) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Text("Choose Game Mode", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(16.dp))

                Difficulty.entries.forEach { difficulty ->
                    Button(
                        onClick = {
                            onDismiss()
                            updateAndSaveUser(context = context, difficulty = difficulty, USER_FN)
                            navController.navigate("${Routes.Game.route}/false/${difficulty.name}")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                    ) {
                        Text(
                            text = difficulty.name.lowercase().replaceFirstChar { it.uppercase() },
                            color = if (difficulty == Difficulty.EXPERT) Color.Red else Color.Black
                        )
                    }

                    Spacer(Modifier.height(8.dp))
                }

                Spacer(Modifier.height(8.dp))

                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancel", color = Color.Black)
                }
            }
        }
    }
}

fun updateAndSaveUser(
    context: Context,
    difficulty: Difficulty,
    filename: String? = USER_FN
) {
    val user = loadUser(context = context, filename = filename)

    val updatedUser = if (user == null) {
        when (difficulty) {
            Difficulty.EASY -> User(
                easy = GameData(0, 1, 0, 0, 0),
                medium = GameData(0, 0, 0, 0, 0),
                hard = GameData(0, 0, 0, 0, 0),
                expert = GameData(0, 0, 0, 0, 0)
            )
            Difficulty.MEDIUM -> User(
                easy = GameData(0, 0, 0, 0, 0),
                medium = GameData(0, 1, 0, 0, 0),
                hard = GameData(0, 0, 0, 0, 0),
                expert = GameData(0, 0, 0, 0, 0)
            )
            Difficulty.HARD -> User(
                easy = GameData(0, 0, 0, 0, 0),
                medium = GameData(0, 0, 0, 0, 0),
                hard = GameData(0, 1, 0, 0, 0),
                expert = GameData(0, 0, 0, 0, 0)
            )
            Difficulty.EXPERT -> User(
                easy = GameData(0, 0, 0, 0, 0),
                medium = GameData(0, 0, 0, 0, 0),
                hard = GameData(0, 0, 0, 0, 0),
                expert = GameData(0, 1, 0, 0, 0)
            )
        }
    } else {
        when (difficulty) {
            Difficulty.EASY -> user.copy(
                easy = user.easy.copy(tries = user.easy.tries + 1)
            )
            Difficulty.MEDIUM -> user.copy(
                medium = user.medium.copy(tries = user.medium.tries + 1)
            )
            Difficulty.HARD -> user.copy(
                hard = user.hard.copy(tries = user.hard.tries + 1)
            )
            Difficulty.EXPERT -> user.copy(
                expert = user.expert.copy(tries = user.expert.tries + 1)
            )
        }
    }

    saveUser(context = context, user = updatedUser, filename = filename)
}