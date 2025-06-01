package com.sonnenstahl.nukodu.ui

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

@Composable
fun BarChart(
    entries: List<BarEntry>,
    labels: List<String>,
    title: String,
    colors: List<Int>? = ColorTemplate.MATERIAL_COLORS.toList(),
    modifier: Modifier = Modifier,
) {
    AndroidView(
        modifier = modifier.then(Modifier.size(300.dp)),
        factory = { context ->
            BarChart(context).apply {
                val dataSet = BarDataSet(entries, title).apply {
                    this.colors = colors
                    valueTextSize = 14f
                }

                data = BarData(dataSet)
                xAxis.apply {
                    granularity = 1f
                    setDrawLabels(true)
                    valueFormatter = IndexAxisValueFormatter(labels)
                    position = com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM
                }

                axisRight.isEnabled = false
                description.isEnabled = false
                animateY(1000)
            }
        }
    )
}