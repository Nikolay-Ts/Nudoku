package com.sonnenstahl


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.sonnenstahl.nukodu.utils.Difficulty
import com.sonnenstahl.nukodu.utils.Routes

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