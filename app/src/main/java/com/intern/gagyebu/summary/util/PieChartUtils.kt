package com.intern.gagyebu.summary.util

import kotlin.math.round

object PieChartUtils {
    fun calculateAngle(
        sliceLength: Float,
        totalLength: Float,
        progress: Float
    ): Float {
        return 360.0f * (sliceLength * progress) / totalLength
    }

    fun calculatePercentage(
        sliceLength: Float,
        totalLength: Float
    ): Int {
        return round(sliceLength / totalLength * 100).toInt()
    }
}