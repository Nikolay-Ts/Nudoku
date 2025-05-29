package com.sonnenstahl.nukodu

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.github.mikephil.charting.data.PieEntry
import com.sonnenstahl.nukodu.utils.USER_FN
import com.sonnenstahl.nukodu.utils.loadUser

@Composable
fun ProfileScreen() {
    val context = LocalContext.current
    val user = loadUser(context, USER_FN)

    if (user == null) {
        Column {
            SvgImageFromAssets("sad-circle.svg", description = "sad-circle")
            Text("There is still no data that we have about you :(")
        }
        return
    }

    val wins = user.easyWins + user.mediumWins + user.hardWins + user.expertWins
    val total =(user.easyTries + user.mediumTries + user.hardTries + user.expertTries)
    val loses = total - wins
    val winLPieEntry =
        listOf(
            PieEntry(wins.toFloat(), "wins"),
            PieEntry(loses.toFloat(), "loses")
        ).filter { it.value > 0 }

    val typeWinsPie = listOf(
        PieEntry(user.easyWins.toFloat(), "easy mode"),
        PieEntry(user.mediumWins.toFloat(), "medium mode"),
        PieEntry(user.hardWins.toFloat(), "hard mode"),
        PieEntry(user.expertWins.toFloat(), "expert mode")
    ).filter { it.value > 0 }


    Column {
        Row(
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            if (winLPieEntry.isNotEmpty()) {
                PieChartView(winLPieEntry, "Total # of Games: $total", Modifier.size(200.dp) )
            }


            if (typeWinsPie.isNotEmpty()) {
                PieChartView(typeWinsPie, "wins per category", Modifier.size(200.dp) )
            }
        }
    }
}