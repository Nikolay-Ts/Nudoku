package com.sonnenstahl.nukodu.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.sonnenstahl.nukodu.utils.User

@SuppressLint("UnrememberedMutableState")
@Composable
fun TimeComparisonChart(user: User, colors: List<Int> = ColorTemplate.MATERIAL_COLORS.toList()) {
    val categories = listOf("Best Time", "Average Time", "Worst Time")
    val selectedCategory = remember { mutableStateOf(categories[0]) }

    val difficultyLabels = listOf("Easy", "Medium", "Hard", "Expert")
    val times = listOf(user.easy, user.medium, user.hard, user.expert)

    val entries by derivedStateOf {
        val values = when (selectedCategory.value) {
            "Best Time" -> times.map { it.bestTime.toFloat() }.filter { it.toInt() != Int.MAX_VALUE }
            "Average Time" -> times.map { it.avgTime.toFloat() }
            "Worst Time" -> times.map { it.worstTime.toFloat() }
            else -> List(4) { 0f }
        }


        values.mapIndexed { index, value -> BarEntry(index.toFloat(), value) }
    }

    val chartRef = remember { mutableStateOf<BarChart?>(null) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        // Dropdown menu
        var expanded by remember { mutableStateOf(false) }

        // Bar chart
        AndroidView(
            factory = { context ->
                BarChart(context).apply {
                    chartRef.value = this

                    xAxis.apply {
                        granularity = 1f
                        valueFormatter = IndexAxisValueFormatter(difficultyLabels)
                        position = com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM
                        setDrawGridLines(false)
                        setDrawAxisLine(true)
                        textSize = 12f
                    }

                    axisLeft.textSize = 12f
                    axisRight.isEnabled = false
                    legend.isEnabled = false
                    description.isEnabled = false
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )

        Box {
            OutlinedButton(
                modifier = Modifier
                    .padding(top = 5.dp),
                shape = RoundedCornerShape(12.dp),
                onClick = { expanded = true }
            ) {
                Text(selectedCategory.value)
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                categories.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category) },
                        onClick = {
                            selectedCategory.value = category
                            expanded = false
                        }
                    )
                }
            }
        }

        // Trigger chart update when the category changes
        LaunchedEffect(entries, selectedCategory.value) {
            chartRef.value?.apply {
                val dataSet = BarDataSet(entries, selectedCategory.value).apply {
                    this.colors = colors
                    valueTextSize = 14f
                }
                data = BarData(dataSet)
                invalidate()
                animateY(800)
            }
        }
    }
}