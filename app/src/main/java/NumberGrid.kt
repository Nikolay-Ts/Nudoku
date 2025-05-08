package com.sonnenstahl.nukodu

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import com.sonnenstahl.nukodu.ui.theme.Background
import com.sonnenstahl.nukodu.ui.theme.LightBlue

@Composable
fun NumberGrid(
    sudokuGrid: Array<IntArray>,
    numberLeft: Array<Int>,
    selectedCell: Pair<Int, Int>?,
    onCellTap: (Int, Int) -> Unit
) {
    val cellSize = 40.dp
    val totalSize = cellSize * 9 //
    val strokeWidth = 10f

    Box (
        modifier = Modifier
            .size(totalSize)
            .background(Background)
    ) {

        Canvas(
            modifier = Modifier
                .size(totalSize)
        ) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val cellWidth = canvasWidth / 9
            val cellHeight = canvasHeight / 9


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
                    start = Offset(0f, i * cellWidth),
                    end = Offset(canvasWidth, i * cellWidth),
                    strokeWidth = stroke
                )
            }

            for (row in 0..8) {
                for (col in 0..8) {
                    val number = sudokuGrid[row][col]

                    if (number != 0) {
                        drawContext.canvas.nativeCanvas.drawText(
                            number.toString(),
                            col * cellWidth + cellWidth / 2,
                            row * cellHeight + cellHeight * 0.7f,
                            android.graphics.Paint().apply {
                                textAlign = android.graphics.Paint.Align.CENTER
                                textSize = cellHeight * 0.7f
                            }
                        )
                    }
                }
            }
        }

        Box(modifier = Modifier
            .matchParentSize()
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    val cellWidthPx = totalSize.toPx() / 9
                    val col = (offset.x / cellWidthPx).toInt()
                    val row = (offset.y / cellWidthPx).toInt()
                    if (row in 0..8 && col in 0..8) {
                        onCellTap(row, col)
                    }
                }
            }
        )
    }
}