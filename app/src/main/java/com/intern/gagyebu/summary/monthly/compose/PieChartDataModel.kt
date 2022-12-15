package com.intern.gagyebu.summary.monthly.compose

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.intern.gagyebu.summary.util.PieChartData

class PieChartDataModel {

    var sliceThickness by mutableStateOf(25f)
    var pieChartData by mutableStateOf(
        PieChartData(
            slices = listOf(
                PieChartData.Slice(
                    percentage = 24f,
                    title = "tt1",
                ),
                PieChartData.Slice(
                    percentage = 48f,
                    "tt2",
                ),
                PieChartData.Slice(
                    percentage = 24f,
                    "tt3",
                )
            )
        )
    )



}