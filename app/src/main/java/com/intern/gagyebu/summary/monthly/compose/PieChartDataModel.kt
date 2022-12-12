package com.intern.gagyebu.summary.monthly.compose

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.intern.gagyebu.summary.util.PieChartData

class PieChartDataModel {
    private var colors = mutableListOf(
        Color(0XFFF44336),
        Color(0XFFE91E63),
        Color(0XFF9C27B0),
        Color(0XFF673AB7),
        Color(0XFF3F51B5),
        Color(0XFF03A9F4),
        Color(0XFF009688),
        Color(0XFFCDDC39),
        Color(0XFFFFC107),
        Color(0XFFFF5722),
        Color(0XFF795548),
        Color(0XFF9E9E9E),
        Color(0XFF607D8B)
    )

    var sliceThickness by mutableStateOf(25f)
    var pieChartData by mutableStateOf(
        PieChartData(
            slices = listOf(
                PieChartData.Slice(
                    percentage = 24f,
                    title = "tt1",
                    randomColor()
                ),
                PieChartData.Slice(
                    percentage = 48f,
                    "tt2",
                    randomColor()
                ),
                PieChartData.Slice(
                    percentage = 24f,
                    "tt3",
                    randomColor()
                )
            )
        )
    )

    /*val slices
        get() = pieChartData.slices*/

    private fun randomColor(): Color {
        val randomIndex = (Math.random() * colors.size).toInt()
        val color = colors[randomIndex]
        colors.removeAt(randomIndex)

        return color
    }

}