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
import androidx.compose.foundation.layout.Arrangement.Vertical
import androidx.compose.ui.unit.dp
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieEntry
import com.sonnenstahl.nukodu.utils.USER_FN
import com.sonnenstahl.nukodu.utils.User
import com.sonnenstahl.nukodu.utils.loadUser
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import com.github.mikephil.charting.data.BarDataSet
import com.sonnenstahl.nukodu.ui.theme.darkBrow
import com.sonnenstahl.nukodu.utils.ExportCsvButton
import com.sonnenstahl.nukodu.utils.GameData
import com.sonnenstahl.nukodu.utils.Routes
import com.sonnenstahl.nukodu.utils.deleteFile
import com.sonnenstahl.nukodu.utils.saveUser
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toLocalDate

enum class ChartMode(val label: String) {
    BY_MONTH("By Month"),
    BY_DAY_OF_WEEK("By Day of Week")
}

/**
 * to display the current data about the user
 * - win/loss ration
 * - which ones have the highest wins
 * - how often does he win in a year/week
 * - best and worst time for each difficulty
 *
 * this should also allow to export the data as a csv or delete it
 */
@Composable
fun ProfileScreen(navController: NavController) {
    val context = LocalContext.current
    val user = loadUser(context, "mock_user.json")
    var chartMode =  remember { mutableStateOf(ChartMode.BY_MONTH) }
    val scrollState = rememberScrollState() // this is to scroll the screen
    val deleteDialog = remember { mutableStateOf(false) }

    if (user == null) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SvgImageFromAssets("sad-circle.svg", description = "sad-circle")
            Text("There is still no data that we have about you :(")
        }
        return
    }

    if (deleteDialog.value) {
        DeleteDialog(
            showDelete = deleteDialog.value,
            onDelete = {
                        deleteFile(context, USER_FN)
                        navController.navigate(Routes.Home.route)
                       },
            onDismiss = { deleteDialog.value = false }
        )
    }

    val wins = user.easy.wins + user.medium.wins + user.hard.wins + user.expert.wins
    val total = user.easy.tries + user.medium.tries + user.hard.tries + user.expert.tries
    val loses = total - wins
    val winLPieEntry =
        listOf(
            PieEntry(wins.toFloat(), "wins"),
            PieEntry(loses.toFloat(), "loses")
        ).filter { it.value > 0 }

    val typeWinsPie = listOf(
        PieEntry(user.easy.wins.toFloat(), "easy mode"),
        PieEntry(user.medium.wins.toFloat(), "medium mode"),
        PieEntry(user.hard.wins.toFloat(), "hard mode"),
        PieEntry(user.expert.wins.toFloat(), "expert mode")
    ).filter { it.value > 0 }

    // i am not filtering the 0s as I think it makes more sense to still display it
    // for a bar chart
    val types = listOf(
        BarEntry(0f, user.easy.wins.toFloat()),
        BarEntry(1f, user.medium.wins.toFloat()),
        BarEntry(2f, user.hard.wins.toFloat()),
        BarEntry(3f, user.expert.wins.toFloat())
    )

    val byMonthData: Pair<List<BarEntry>, List<String>> = run {
        val grouped = user.gameCompletionDates
            .groupingBy { "${it.month.name} ${it.year}" }
            .eachCount()
            .toSortedMap()

        val entries = grouped.entries.mapIndexed { i, entry ->
            BarEntry(i.toFloat(), entry.value.toFloat())
        }

        val labels = grouped.keys.toList()

        entries to labels
    }
    val byDayOfWeekData: Pair<List<BarEntry>, List<String>> = run {
        val grouped = user.gameCompletionDates
            .groupingBy { it.dayOfWeek.name.lowercase().replaceFirstChar { c -> c.uppercase() } }
            .eachCount()

        val orderedDays = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
        val sorted = orderedDays.mapNotNull { day ->
            grouped[day]?.let { day to it }
        }.toMap()

        val entries = sorted.entries.mapIndexed { i, entry ->
            BarEntry(i.toFloat(), entry.value.toFloat())
        }

        val labels = sorted.keys.toList()

        entries to labels
    }

    Column(
        verticalArrangement = Arrangement.Top,
        modifier = Modifier.verticalScroll(scrollState)
    ) {
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

        BarChart(
            types,
            listOf("Easy", "Medium", "Hard", "Expert"),
            "Wins per Difficulty",
            modifier =  Modifier.fillMaxWidth())

        Column {
            when (chartMode.value) {
                ChartMode.BY_DAY_OF_WEEK -> BarChart(
                    byDayOfWeekData.first,
                    byDayOfWeekData.second,
                    "Number of games finished"
                )
                ChartMode.BY_MONTH -> BarChart(
                    byMonthData.first,
                    byMonthData.second,
                    "Number of games finished"
                )
            }

            ChartModeDropdown(selectedMode = chartMode.value, onModeSelected = { chartMode.value = it })
        }


        Row(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 60.dp, bottom = 40.dp)

        ) {
            ExportCsvButton(user, context)

            Spacer(Modifier.padding(10.dp))

            Button(onClick = { deleteDialog.value = true }) {
                Text("Delete Account")
            }
        }

    }
}

@Composable
fun ChartModeDropdown(
    selectedMode: ChartMode,
    onModeSelected: (ChartMode) -> Unit
) {
    val expanded = remember { mutableStateOf(false) }

    Box {
        Button(onClick = { expanded.value = true }) {
            Text(selectedMode.label)
        }

        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false }
        ) {
            ChartMode.entries.forEach { mode ->
                DropdownMenuItem(
                    text = { Text(mode.label) },
                    onClick = {
                        onModeSelected(mode)
                        expanded.value = false
                    }
                )
            }
        }
    }
}