package com.sonnenstahl.nukodu

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.input.pointer.pointerInput
import com.sonnenstahl.nukodu.ui.theme.Background
import com.sonnenstahl.nukodu.utils.Tile


@Composable
fun NumberGrid(
    sudokuGrid: Array<Array<Tile>>,
    selectedCell: Pair<Int, Int>?,
    onCellTap: (Int, Int) -> Unit
) {
    val cellSize = 40.dp
    val totalSize = cellSize * 9

    Box(
        modifier = Modifier
            .size(totalSize)
            .background(Background)
    ) {
        Canvas(
            modifier = Modifier.size(totalSize)
        ) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val cellWidth = canvasWidth / 9
            val cellHeight = canvasHeight / 9

            // highlights the currently selected cell & like a cross for better visuals
            // must be done first to be rendered as a background
            if (selectedCell != null) {
                val (selectedRow, selectedCol) = selectedCell

                for (row in 0..8) {
                    drawRect(
                        color = Color(0xf0ececec),
                        topLeft = Offset(
                            x = selectedCol * cellWidth,
                            y = row * cellHeight
                        ),
                        size = androidx.compose.ui.geometry.Size(cellWidth, cellHeight)
                    )
                }

                for (col in 0..8) {
                    drawRect(
                        color = Color(0xf0ececec),
                        topLeft = Offset(
                            x = col * cellWidth,
                            y = selectedRow * cellHeight
                        ),
                        size = androidx.compose.ui.geometry.Size(cellWidth, cellHeight)
                    )
                }
            }

            // Draw grid lines
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

            drawIntoCanvas { canvas ->
                val paint = android.graphics.Paint().apply {
                    textAlign = android.graphics.Paint.Align.CENTER
                    isAntiAlias = true
                    color = android.graphics.Color.BLACK
                    textSize = cellHeight * 0.7f

                }

                for (row in 0..8) {
                    for (col in 0..8) {
                        val number = sudokuGrid[row][col].number
                        if (number != 0) {
                            val x = col * cellWidth + cellWidth / 2
                            val y = row * cellHeight + cellHeight * 0.7f
                            canvas.nativeCanvas.drawText(
                                number.toString(),
                                x,
                                y,
                                paint
                            )
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
                        val cellWidthPx = cellSize.toPx()
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