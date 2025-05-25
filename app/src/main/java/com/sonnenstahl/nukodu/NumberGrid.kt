package com.sonnenstahl.nukodu

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.sonnenstahl.nukodu.ui.theme.Background
import utils.Tile

@Composable
fun NumberGrid(
    sudokuGrid: Array<Array<Tile>>,
    selectedCell: Pair<Int, Int>?,
    currentlySelected: Int,
    onCellTap: (Int, Int) -> Unit
) {

    val canvasSize = remember { mutableStateOf(androidx.compose.ui.geometry.Size.Zero) }

    Box(
        modifier = Modifier
            .background(Background)
            .size(400.dp) // Makes grid square
            .fillMaxWidth()
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            // store canvas
            canvasSize.value = size

            val canvasWidth = size.width
            val canvasHeight = size.height
            val cellWidth = canvasWidth / 9
            val cellHeight = canvasHeight / 9

            // highlight all numbers that are the same as the one that I pressed
            for (row in 0..8) {
                for (col in 0..8) {
                    val iNumber = sudokuGrid[row][col]
                    if (iNumber.number == 0) {
                        continue
                    }
                    if (iNumber.number == currentlySelected) {
                        drawRect(
                            color = Color(0xFFECEFF1),
                            topLeft = Offset(col * cellWidth, row * cellHeight),
                            size = androidx.compose.ui.geometry.Size(cellWidth, cellHeight)
                        )
                    }
                }
            }

            selectedCell?.let { (selectedRow, selectedCol) ->
                for (row in 0..8) {
                    drawRect(
                        color = Color(0xFFECEFF1),
                        topLeft = Offset(selectedCol * cellWidth, row * cellHeight),
                        size = androidx.compose.ui.geometry.Size(cellWidth, cellHeight)
                    )
                }
                for (col in 0..8) {
                    drawRect(
                        color = Color(0xFFECEFF1),
                        topLeft = Offset(col * cellWidth, selectedRow * cellHeight),
                        size = androidx.compose.ui.geometry.Size(cellWidth, cellHeight)
                    )
                }
            }

            // draw grid lines
            for (i in 0..9) {
                val stroke = if (i % 3 == 0) 4f else 1f
                drawLine(
                    color = Color.Black,
                    start = Offset(i * cellWidth, 0f),
                    end = Offset(i * cellWidth, canvasHeight),
                    strokeWidth = stroke
                )
                drawLine(
                    color = Color.Black,
                    start = Offset(0f, i * cellHeight),
                    end = Offset(canvasWidth, i * cellHeight),
                    strokeWidth = stroke
                )
            }

            // draw numbers
            drawIntoCanvas { canvas ->
                val paint = android.graphics.Paint().apply {
                    textAlign = android.graphics.Paint.Align.CENTER
                    isAntiAlias = true
                    textSize = cellHeight * 0.7f
                }

                for (row in 0..8) {
                    for (col in 0..8) {
                        val cellTile = sudokuGrid[row][col]
                        val number = cellTile.number

                        paint.color = if (cellTile.isCompleted) {
                            android.graphics.Color.BLACK
                        } else {
                            android.graphics.Color.RED
                        }

                        if (number != 0) {
                            val x = col * cellWidth + cellWidth / 2
                            val y = row * cellHeight + cellHeight * 0.7f
                            canvas.nativeCanvas.drawText(number.toString(), x, y, paint)
                        }
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .matchParentSize()
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        val cellWidth = canvasSize.value.width / 9
                        val cellHeight = canvasSize.value.height / 9
                        val col = (offset.x / cellWidth).toInt()
                        val row = (offset.y / cellHeight).toInt()
                        if (row in 0..8 && col in 0..8) {
                            onCellTap(row, col)
                        }
                    }
                }
        )
    }
}