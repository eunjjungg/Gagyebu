package com.intern.gagyebu.summary.util

import androidx.compose.ui.graphics.Color

class PieChartData(
    var slices: List<Slice>
) {
    internal val totalSize: Float
        get() {
            var total = 0f
            slices.forEach { total += it.percentage }
            return total
        }

    data class Slice(
        val percentage: Float,
        val title: String,
    )
}