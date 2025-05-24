package com.sonnenstahl.nukodu.com.sonnenstahl.nukodu

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sonnenstahl.nukodu.ui.theme.LightBlue

/**
 * This is to init the numbers for the user to select when to place them
 * hidden is this is so that the user cannot select a button of a number that
 * has been used up
 */
@Composable
fun NumberButtons(
    number: Int,
    isSelected: Boolean,
    enabled: Boolean,
    numbersLeft: SnapshotStateMap<Int, Int>,
    canDissapear: SnapshotStateMap<Int, Boolean>,
    onClick: () -> Unit // TODO: need to add logic to not allow more than 9 members on board
) {
    val isEnabled = enabled && !canDissapear[number]!!
    Button(
        onClick = onClick,
        enabled = isEnabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = if (isSelected) LightBlue else Color.Black,
            disabledContainerColor = Color.Transparent
        ),
        elevation = null,
        contentPadding = PaddingValues(0.dp),
        modifier = Modifier
            .width(25.dp)
            .height(48.dp)

    ) {
        Text(
            text = if (numbersLeft[number]!! == 0) " " else number.toString(),
            fontSize = 30.sp, style = MaterialTheme.typography.bodyLarge)
    }
}