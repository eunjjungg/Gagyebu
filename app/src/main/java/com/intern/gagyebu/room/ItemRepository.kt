package com.intern.gagyebu.room

import com.intern.gagyebu.summary.monthly.PieChartView
import com.intern.gagyebu.summary.util.CategoryInfoOfMonth
import com.intern.gagyebu.summary.util.MonthlyCategory
import com.intern.gagyebu.summary.util.SumOfCategory

class ItemRepository2(private val itemDao: ItemDao) {

    fun getAmountWhenYearAndMonthSet(year: Int, month: Int) : List<Int> {
        return itemDao.getAmountWhenYearAndMonthSet(year, month)
    }

    fun getAmountSumOfEachCategory(year: Int) : List<SumOfCategory> {
        return itemDao.getAmountSumOfEachCategory(year)
    }

    fun getCategorySumOfEachMonth(year: Int, category: String) : List<MonthlyCategory> {
        return itemDao.getCategorySumOfEachMonth(year, category)
    }

    fun getCategoryAndSumWhenYearAndMonthSet(year: Int, month: Int) : List<CategoryInfoOfMonth> {
        return itemDao.getCategoryAndSumWhenYearAndMonthSet(year, month)
    }
}