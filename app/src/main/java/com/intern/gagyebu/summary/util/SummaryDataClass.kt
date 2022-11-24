package com.intern.gagyebu.summary.util

import androidx.room.ColumnInfo

data class SumOfCategory(
    @ColumnInfo(name = "category")
    val category: String,
    @ColumnInfo(name = "sum(amount)")
    val sum: Int
)

data class MonthlyCategory(
    @ColumnInfo(name = "month")
    val month: Int,
    @ColumnInfo(name = "sum(amount)")
    val sum: Int
)

data class BarChartInfo(
    val month: Int,
    var percentage: Float
)

data class ReportViewInfo(
    val theme: String,
    val average: String,
    val cost1: String,
    val cost2: String?,
    val cost3: String?
)