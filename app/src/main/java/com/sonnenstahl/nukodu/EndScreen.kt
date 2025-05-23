package com.sonnenstahl.nukodu.com.sonnenstahl.nukodu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import com.sonnenstahl.nukodu.ui.theme.NukoduTheme

class EndScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NukoduTheme {
                EndScreenComponent()
            }
        }
    }
}

@Composable
fun EndScreenComponent() {

}



