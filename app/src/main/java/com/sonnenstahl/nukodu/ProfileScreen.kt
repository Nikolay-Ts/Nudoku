package com.sonnenstahl.nukodu

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieEntry
import com.sonnenstahl.nukodu.utils.USER_FN
import com.sonnenstahl.nukodu.utils.User
import com.sonnenstahl.nukodu.utils.loadUser
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding

@Composable
fun ProfileScreen() {
    val context = LocalContext.current
    val user = loadUser(context, "mock_user.json")

    if (user == null) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
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

    // i am not filtering the 0s as I think it makes more sense to still display it
    // for a bar chart
    val barEntries = listOf(
        BarEntry(0f, user.easyWins.toFloat()),
        BarEntry(1f, user.mediumWins.toFloat()),
        BarEntry(2f, user.hardWins.toFloat()),
        BarEntry(3f, user.expertWins.toFloat())
    )
    val labels = listOf("Easy", "Medium", "Hard", "Expert")


    Column(verticalArrangement = Arrangement.Top) {
        Text("Stats About Your Games!", Modifier
            .padding(bottom = 30.dp, top = 50.dp)
            .align(Alignment.CenterHorizontally)
        )
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

        BarChart(barEntries, labels, Modifier.fillMaxWidth())
    }
}