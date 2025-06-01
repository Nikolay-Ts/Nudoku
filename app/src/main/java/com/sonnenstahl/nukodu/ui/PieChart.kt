package com.sonnenstahl.nukodu.ui

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate


/**
 * Give it the data and title and it will plot it
 */
@Composable
fun PieChartView(
    data: List<PieEntry>,
    title: String,
    colors: List<Int> = ColorTemplate.COLORFUL_COLORS.toList(),
    modifier: Modifier = Modifier.size(250.dp)) {
    AndroidView(factory = { context ->
        PieChart(context).apply {
            val dataSet = PieDataSet(data, title)
            dataSet.colors = colors
            this.data = PieData(dataSet)

            description.isEnabled = false
            isDrawHoleEnabled = false
            animateY(1000)
        }
    },
        modifier = modifier
    )
}