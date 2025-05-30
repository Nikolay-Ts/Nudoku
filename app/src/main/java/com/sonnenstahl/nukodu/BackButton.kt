package com.sonnenstahl.nukodu

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

/**
 * Button to go to the previous page, should be everywhere but EndScreen
 */
@Composable
fun GoBack(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(align = Alignment.TopStart)
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier
                .align(Alignment.TopStart),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            contentPadding = PaddingValues(0.dp)
        ) {
            SvgImageFromAssets(
                filepath = "back-logo.svg",
                modifier = Modifier.size(30.dp),
                scale = 0.5f
            )
        }
    }
}