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
import com.sonnenstahl.nukodu.ui.theme.Background
import com.sonnenstahl.nukodu.ui.theme.NukoduTheme
import com.sonnenstahl.nukodu.utils.createNudoku


class NudokuView : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NukoduTheme {
                NudokuScreen()
            }
        }
    }
}

@Composable
fun NudokuScreen() {
    var currentlySelected by remember { mutableIntStateOf (0) }
    val nudokuGrid = remember { Array(9) { IntArray(9) } }
    var selectedCell by remember { mutableStateOf<Pair<Int,Int>?>(null) }
    // the * is used to tell Compose that this array will be dynamically modified in functions
    val numbersLeft = remember { mutableStateMapOf(*(1..9).map { it to 9 }.toTypedArray() ) }

    // creates the grid only once
    LaunchedEffect(Unit) {
        createNudoku(nudokuGrid, numbersLeft, 10)
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
            onCellTap = { i, j ->
                selectedCell = i to j
                if (currentlySelected in 1..9) {
                    nudokuGrid[i][j] = currentlySelected
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
                    hidden = (numbersLeft[i]!! != 0),
                    numbersLeft = numbersLeft
                ) {
                    if (numbersLeft[i]!! > 0 ){
                        currentlySelected = i
                    }
                    Log.d("numbers left:", numbersLeft.toString())
                }
            }
        }
    }
}

