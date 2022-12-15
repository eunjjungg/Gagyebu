package com.intern.gagyebu.summary.util

import android.os.Parcelable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.room.ColumnInfo
import com.intern.gagyebu.room.ItemEntity
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

data class CategoryInfoOfMonth(
    @ColumnInfo(name = "category")
    val category: String,
    @ColumnInfo(name = "sum(amount)")
    val sum: Int
)

@Parcelize
data class PieElement(
    val name: String,
    val percentage: Float
): Parcelable

@Parcelize
data class DateInfo(
    val year: Int,
    val month: Int
): Parcelable

data class MonthlyDetailInfo(
    val item: ItemEntity,
    val percentage: Int
)

data class MonthlyDetailInfoWithState(
    val item: ItemEntity,
    val order: Int,
    var isOpen: MutableState<Boolean> = mutableStateOf(false)
)