package com.sonnenstahl.nukodu
import android.os.Bundle
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
import androidx.compose.ui.modifier.modifierLocalConsumer
import com.sonnenstahl.nukodu.ui.theme.Background
import com.sonnenstahl.nukodu.ui.theme.NukoduTheme


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
    val numbersleft = remember { mutableStateListOf(*Array(9) { 9 } ) }

    // creates the grid only once
    LaunchedEffect(Unit) {
        createNudoku(nudokuGrid, 10)
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
            numberLeft = numbersleft,
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
                    isSelected = (currentlySelected == i)) {
                    currentlySelected = i
                }
            }
        }
    }
}

