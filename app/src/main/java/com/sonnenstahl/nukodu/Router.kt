package com.sonnenstahl.nukodu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController
import utils.Routes

@Composable
fun Router(navController: NavHostController){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White), contentAlignment = Alignment.Center
    ) {
        Column {

            Spacer(modifier = Modifier.height(20.dp))

            // Navigate to Profile Screen
            Button(onClick = {
                navController.navigate(Routes.Game.route)
            }) {
                Text("New Game")
            }
        }
    }
}