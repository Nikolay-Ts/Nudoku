package com.sonnenstahl.nukodu

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sonnenstahl.nukodu.ui.theme.LightBlue

/**
 * This is to init the numbers for the user to select when to place them
 */
@Composable
fun NumberButtons(
    number: Int,
    isSelected: Boolean,
    onClick: () -> Unit // TODO: need to add logic to not allow more than 9 members on board
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = if (isSelected) LightBlue else Color.Black
        ),
        elevation = null,
        contentPadding = PaddingValues(0.dp),
        modifier = Modifier
            .width(25.dp)
            .height(48.dp)
    ) {
        Text(text = number.toString(), fontSize = 30.sp, style = MaterialTheme.typography.bodyLarge)
    }
}