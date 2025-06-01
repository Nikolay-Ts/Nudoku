package com.sonnenstahl.nukodu.ui

import androidx.activity.compose.BackHandler
import com.sonnenstahl.nukodu.utils.importLoadedGame
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.sonnenstahl.nukodu.ui.theme.Background
import com.sonnenstahl.nukodu.utils.CURRENT_GAME_FN
import com.sonnenstahl.nukodu.utils.Difficulty
import com.sonnenstahl.nukodu.utils.createNudoku
import com.sonnenstahl.nukodu.utils.Tile
import kotlinx.coroutines.delay
import com.sonnenstahl.nukodu.utils.GameState
import com.sonnenstahl.nukodu.utils.Pos
import com.sonnenstahl.nukodu.utils.Routes
import com.sonnenstahl.nukodu.utils.deleteFile
import com.sonnenstahl.nukodu.utils.updateAndSave
import com.sonnenstahl.nukodu.utils.loadGame
import com.sonnenstahl.nukodu.utils.placeNumber
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

@Composable
fun NudokuScreen(navController: NavController, currentGameFile: Boolean, difficulty: Difficulty) {

    var currentlySelected by remember { mutableIntStateOf(0) }
    var selectedCell by remember { mutableStateOf<Pair<Int, Int>?>(null) }
    val errors = remember { mutableIntStateOf(0) }
    val nudokuGrid = remember {
        Array(9) { row -> Array(9) { col -> Tile(cell = Pos(row, col)) } }
    }
    val numbersLeft = remember { mutableStateMapOf(*(1..9).map { it to 9 }.toTypedArray()) }
    val gameState = remember { mutableStateOf(GameState.RUNNING) }
    val gameTimeSeconds = remember { mutableIntStateOf(0) }
    val eraserMode = remember { mutableStateOf(false) }

    val context = LocalContext.current
    val saveGame = remember { Mutex() }

    // this is so that after playing a new game from EndScreen, you still go back to the main menu
    BackHandler(enabled = true) { navController.navigate(Routes.Home.route)}

    LaunchedEffect(Unit) {
        when (currentGameFile) {
            true -> { // loads the game from disk
                val game = loadGame(context, CURRENT_GAME_FN)
                if (game == null) {
                    navController.navigate(Routes.Home)
                    return@LaunchedEffect
                }
                importLoadedGame(
                    game = game,
                    nudokuGrid = nudokuGrid,
                    numbersLeft = numbersLeft,
                    errors = errors,
                    gameState = gameState,
                    gameTimeSeconds = gameTimeSeconds
                )
            }

            false -> { // creates a new game and saves to disk
                val removeNum = when (difficulty) {
                    Difficulty.EASY -> 1
                    Difficulty.MEDIUM -> 30
                    Difficulty.HARD -> 40
                    Difficulty.EXPERT -> 50
                }

                createNudoku(nudokuGrid, numbersLeft, removeNum)
                updateAndSave(
                    difficulty = Difficulty.EASY,
                    nudokuGrid = nudokuGrid,
                    errors = errors.intValue,
                    gameState = gameState.value,
                    time = gameTimeSeconds.intValue,
                    context = context
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000L)
            saveGame.withLock {
                if (gameState.value == GameState.RUNNING) {
                    gameTimeSeconds.intValue++
                }


                updateAndSave(
                    difficulty = difficulty,
                    nudokuGrid = nudokuGrid,
                    errors = errors.intValue,
                    gameState = gameState.value,
                    time = gameTimeSeconds.intValue,
                    context = context
                )
            }
        }
    }

    LaunchedEffect(numbersLeft, errors.intValue) {
        saveGame.withLock {
            updateAndSave(
                difficulty = difficulty,
                nudokuGrid = nudokuGrid,
                errors = errors.intValue,
                gameState = gameState.value,
                time = gameTimeSeconds.intValue,
                context = context
            )
        }
    }


    LaunchedEffect(gameState.value) {
        if (gameState.value == GameState.WON || gameState.value == GameState.LOST) {
            saveGame.withLock {
                updateAndSave(
                    difficulty = difficulty,
                    nudokuGrid = nudokuGrid,
                    errors = errors.intValue,
                    gameState = gameState.value,
                    time = gameTimeSeconds.intValue,
                    context = context
                )

                navController.navigate(
                    "${Routes.EndScreen.route}/" +
                            "${gameState.value.name}/" +
                            "${difficulty.name}/" +
                            "${gameTimeSeconds.intValue}/" +
                            "${errors.intValue}"
                )
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (gameState.value == GameState.PAUSED) {
            PauseConfirmationDialog(
                onContinue = { gameState.value = GameState.RUNNING },
                onQuit = {
                    deleteFile(context, CURRENT_GAME_FN)
                    navController.navigate(Routes.Home.route)
                },
                onDismiss = { gameState.value = GameState.RUNNING }
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Background)
                    .padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // timer and errors
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val minutes = (gameTimeSeconds.intValue % 3600) / 60
                    val seconds = gameTimeSeconds.intValue % 60

                    Text(
                        text = String.format("%02d:%02d", minutes, seconds),
                        color = Color.Gray,
                        modifier = Modifier
                            .padding(vertical = 8.dp),
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "${errors.intValue}/3",
                        modifier = Modifier.padding(vertical = 8.dp),
                        textAlign = TextAlign.End,
                        color = Color.LightGray
                    )
                }

                NumberGrid(
                    sudokuGrid = nudokuGrid,
                    selectedCell = selectedCell,
                    currentlySelected = currentlySelected,
                    onCellTap = { i, j ->
                        if (Pair(i, j) == selectedCell) {
                            selectedCell = null
                            return@NumberGrid
                        }

                        if (eraserMode.value && !nudokuGrid[i][j].isCompleted) {
                            val number = nudokuGrid[i][j].number
                            if (number != 0) {
                                numbersLeft[number] = numbersLeft[number]!! + 1
                                nudokuGrid[i][j].number = 0
                            }
                            selectedCell = null
                            return@NumberGrid
                        }

                        // this is so that when a user wants to see all of the numbers
                        // for a specific cell, it will not highlight the cells of the currently
                        // chosen number
                        if (nudokuGrid[i][j].number != currentlySelected && nudokuGrid[i][j].isCompleted) {
                            currentlySelected = 0
                        }

                        selectedCell = i to j
                        placeNumber(
                            i, j,
                            currentlySelected,
                            nudokuGrid,
                            numbersLeft,
                            errors,
                            gameState,
                            context,
                        ) {
                            selectedCell = null
                        }
                    }
                )
            }

            // eraser and pause buttons
            Row(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 20.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        eraserMode.value = !eraserMode.value
                        currentlySelected = 0
                    },
                    modifier = Modifier.size(100.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    )
                ) {
                    val iconFile = if (eraserMode.value) "selected_eraser.svg" else "eraser.svg"
                    SvgImageFromAssets(
                        filepath = iconFile,
                        modifier = Modifier.size(60.dp),
                        scale = 5.0F
                    )
                }

                Button(
                    modifier = Modifier.size(100.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    onClick = {
                    when (gameState.value) {
                        GameState.RUNNING -> gameState.value = GameState.PAUSED
                        GameState.PAUSED -> gameState.value = GameState.RUNNING
                        else -> {}
                    }
                }) {
                    val iconFile = if (gameState.value == GameState.RUNNING ) "pause.svg" else "play.svg"
                    SvgImageFromAssets(
                        filepath = iconFile,
                        modifier = Modifier.size(60.dp),
                        scale = 5.0F
                    )
                }


            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 20.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (i in 1..9) {
                    NumberButtons(
                        number = i,
                        isSelected = (currentlySelected == i && numbersLeft[i]!! > 0),
                        enabled = (numbersLeft[i]!! != 0),
                        numbersLeft = numbersLeft,
                    ) {
                        currentlySelected = if (currentlySelected != i) i else 0
                        eraserMode.value = false

                        selectedCell?.let { (row, col) ->
                            val number = nudokuGrid[row][col].number
                            if (currentlySelected != number && number != 0) {
                                selectedCell = null
                                return@NumberButtons
                            }

                            placeNumber(
                                row, col,
                                currentlySelected,
                                nudokuGrid,
                                numbersLeft,
                                errors,
                                gameState,
                                context,
                            ) {
                                selectedCell = null
                            }
                        }
                    }
                }
            }
        }

        GoBack { navController.navigate(Routes.Home.route) }
    }
}
