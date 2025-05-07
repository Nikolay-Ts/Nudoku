package com.sonnenstahl.nukodu
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.*
import com.sonnenstahl.nukodu.ui.theme.Background
import com.sonnenstahl.nukodu.ui.theme.NukoduTheme
import com.sonnenstahl.nukodu.ui.theme.LightBlue


/**
 * This is to init the numbers for the user to select when to place them
 */
class Buttons : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NukoduTheme {
                ButtonRowScreen()
            }
        }
    }
}

@Composable
fun ButtonRowScreen() {
    var currentlySelected by remember { mutableIntStateOf (0) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(bottom = 32.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            for (i in 1..9) {
                NumberButtons(number = i, isSelected = (currentlySelected == i)) {
                    currentlySelected = i
                }
            }
        }
    }
}


@Composable
fun NumberButtons(number: Int, isSelected: Boolean, onClick: () -> Unit) {
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