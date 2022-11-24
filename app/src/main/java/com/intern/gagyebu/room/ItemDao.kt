package com.intern.gagyebu.room

import androidx.room.Insert
import androidx.room.Query
import com.intern.gagyebu.summary.util.MonthlyCategory
import com.intern.gagyebu.summary.util.SumOfCategory
import kotlinx.coroutines.flow.Flow

@androidx.room.Dao
interface ItemDao{

    @Query("SELECT * FROM ItemEntity WHERE month = :month AND year = :year")
    fun getItemWhenYearAndMonthSet(year: Int, month: Int) : List<ItemEntity>

    @Query("SELECT amount FROM ItemEntity WHERE month = :month AND year = :year")
    fun getAmountWhenYearAndMonthSet(year: Int, month: Int) : List<Int>

    @Query("SELECT category, sum(amount) FROM ItemEntity WHERE year = :year GROUP BY category")
    fun getAmountSumOfEachCategory(year: Int) : List<SumOfCategory>

    @Query("SELECT month, sum(amount) FROM ItemEntity WHERE year = :year AND category = :category GROUP BY month ORDER BY sum(amount) desc")
    fun getCategorySumOfEachMonth(year: Int, category: String) : List<MonthlyCategory>
}