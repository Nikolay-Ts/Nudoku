package com.sonnenstahl.nukodu.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sonnenstahl.nukodu.ui.theme.LightBlue

@Composable
fun NudokuLogo(modifier: Modifier = Modifier) {
    val letters = listOf(
        "N", "U", "",
        "", "D", "O",
        "K", "U", ""
    )
    val blueLetter = remember { mutableStateListOf(*Array (9) { false  }) }

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .padding(8.dp)
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            for (row in 0 until 3) {
                Row(modifier = Modifier.weight(1f)) {
                    for (col in 0 until 3) {
                        val index = row * 3 + col
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .border(2.dp, Color.Black),
                            contentAlignment = Alignment.Center
                        ) {
                            Button(
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Transparent
                                ),
                                onClick = { blueLetter[index] = !blueLetter[index] }
                            ) {

                                Text(
                                    text = letters[index],
                                    style = MaterialTheme.typography.headlineLarge.copy(
                                        fontSize = 48.sp,
                                        color =  if (blueLetter[index]) LightBlue else Color.Black
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
