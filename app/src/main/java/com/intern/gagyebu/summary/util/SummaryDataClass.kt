package com.intern.gagyebu.summary.util

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import kotlinx.parcelize.Parcelize

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

@Parcelize
data class DateInfo(
    val year: Int,
    val month: Int
): Parcelable