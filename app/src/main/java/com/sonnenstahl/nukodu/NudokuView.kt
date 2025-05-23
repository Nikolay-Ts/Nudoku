package com.sonnenstahl.nukodu

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import androidx.navigation.NavController
import com.sonnenstahl.nukodu.com.sonnenstahl.nukodu.NumberButtons
import com.sonnenstahl.nukodu.com.sonnenstahl.nukodu.NumberGrid
import com.sonnenstahl.nukodu.ui.theme.Background
import com.sonnenstahl.nukodu.ui.theme.NukoduTheme
import com.sonnenstahl.nukodu.utils.createNudoku
import com.sonnenstahl.nukodu.utils.Tile
import com.sonnenstahl.nukodu.utils.validateTile
import utils.GameState
import utils.Routes
import utils.gameStateChecker

// TODO: erase button
// TODO: undo?
// TODO: Timer

@Composable
fun NudokuScreen(navController: NavController) {

    var currentlySelected by remember { mutableIntStateOf (0) }
    var selectedCell by remember { mutableStateOf<Pair<Int,Int>?>(null) }
    var errors = remember { mutableIntStateOf(0) }

    val nudokuGrid = remember {
        Array(9) { row ->
            Array(9) { col ->
                Tile(cell = Pair(row, col))
            }
        }
    }
    // the * is used to tell Compose that this array will be dynamically modified in functions
    val numbersLeft = remember { mutableStateMapOf(*(1..9).map { it to 9 }.toTypedArray() ) }
    val numbersDissapear = remember { mutableStateMapOf(*(1..9).map { it to false}.toTypedArray() )}
    val gameState = remember { mutableStateOf<GameState>(GameState.RUNNING) }


    // creates the grid only once
    LaunchedEffect(Unit) {
        createNudoku(nudokuGrid, numbersLeft, 1)
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

        Spacer(modifier = Modifier.height(32.dp))
        
        NumberGrid(
            sudokuGrid = nudokuGrid,
            selectedCell = selectedCell,
            // TODO: if it is wrong and then you override it, add the overiden number back
            onCellTap = { i, j ->
                selectedCell = i to j
                if (currentlySelected in 1..9 && !nudokuGrid[i][j].isCompleted && numbersLeft[currentlySelected]!! > 0) {
                    val cellTile = nudokuGrid[i][j]
                    val currentCellNumber = cellTile.number

                    cellTile.number = currentlySelected

                    if (numbersLeft[currentCellNumber] != null) {
                        numbersLeft[currentCellNumber] = numbersLeft[currentCellNumber]!! + 1
                        // it was 0 and now must be reEnabled
                        if (numbersLeft[currentCellNumber] == 1) {
                            numbersDissapear[currentCellNumber] = false
                        }
                    }


                    cellTile.isCompleted = validateTile(nudokuGrid, i, j, currentlySelected)

                    numbersLeft[currentlySelected] = numbersLeft[currentlySelected]!! - 1
                    Log.d("meow", "completed: ${cellTile.isCompleted}, num ${cellTile.number}" )

                    if (numbersLeft[currentlySelected]!! == 0 && !cellTile.isCompleted) {
                            numbersDissapear[cellTile.number] = true
                            Log.d("Cannot disappear", "number: ${cellTile.number},${numbersDissapear[cellTile.number]}")

                    }

                    if (numbersLeft[currentlySelected]!! == 0 && cellTile.isCompleted) {
                            numbersDissapear[cellTile.number] = false
                    }

                    gameStateChecker(numbersLeft, errors, gameState)


                    Log.d("GameState" ,"$gameState")
                    Log.d("Validation" ,"cell: $i, $j\n is valid?:${cellTile.isCompleted}")
                }
            }
        )


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            for (i in 1..9) {
                NumberButtons(
                    number = i,
                    isSelected = (currentlySelected == i && numbersLeft[i]!! > 0 ),
                    enabled = (numbersLeft[i]!! != 0),
                    numbersLeft = numbersLeft,
                    canDissapear = numbersDissapear
                ) {
                    if (numbersLeft[i]!! > 0){
                        currentlySelected = i
                    }
                }
            }
        }
    }
}

