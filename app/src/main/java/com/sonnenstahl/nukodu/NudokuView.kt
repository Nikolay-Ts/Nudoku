package com.sonnenstahl.nukodu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.sonnenstahl.nukodu.com.sonnenstahl.nukodu.NumberButtons
import com.sonnenstahl.nukodu.ui.theme.Background
import com.sonnenstahl.nukodu.utils.createNudoku
import com.sonnenstahl.nukodu.utils.Tile
import kotlinx.coroutines.delay
import utils.GameState
import utils.Routes
import utils.placeNumber

@Composable
fun NudokuScreen(navController: NavController) {

    var currentlySelected by remember { mutableIntStateOf(0) }
    var selectedCell by remember { mutableStateOf<Pair<Int, Int>?>(null) }
    val errors = remember { mutableIntStateOf(0) }

    val nudokuGrid = remember {
        Array(9) { row ->
            Array(9) { col ->
                Tile(cell = Pair(row, col))
            }
        }
    }

    val numbersLeft = remember { mutableStateMapOf(*(1..9).map { it to 9 }.toTypedArray()) }
    val numbersDissapear = remember { mutableStateMapOf(*(1..9).map { it to false }.toTypedArray()) }
    val gameState = remember { mutableStateOf(GameState.RUNNING) }
    val gameTimeSeconds = remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        createNudoku(nudokuGrid, numbersLeft, 10)
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000L)
            gameTimeSeconds.intValue++
        }
    }

    LaunchedEffect(gameState.value) {
        if (gameState.value == GameState.WON || gameState.value == GameState.LOST) {
            navController.navigate("${Routes.EndScreen.route}/${gameState.value.name}")
        }
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
            Row (
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

                    selectedCell = i to j
                    placeNumber(
                        i, j,
                        currentlySelected,
                        nudokuGrid,
                        numbersLeft,
                        numbersDissapear,
                        errors,
                        gameState
                    ) {
                        selectedCell = null
                        currentlySelected = 0
                    }
                }
            )
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
                    canDissapear = numbersDissapear
                ) {
                    currentlySelected = if (currentlySelected != i) i else 0
                    selectedCell?.let { (row, col) ->
                        placeNumber(row, col, currentlySelected, nudokuGrid, numbersLeft, numbersDissapear, errors, gameState) {
                            selectedCell = null
                            currentlySelected = 0
                        }
                    }
                }
            }
        }
    }
}
