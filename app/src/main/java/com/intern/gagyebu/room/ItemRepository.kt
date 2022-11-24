package com.intern.gagyebu.room

import com.intern.gagyebu.summary.util.MonthlyCategory
import com.intern.gagyebu.summary.util.SumOfCategory

class ItemRepository(private val itemDao: ItemDao) {
    suspend fun getItemsWhenYearAndMonthSet(year: Int, month: Int)
    : List<ItemEntity> {
        return itemDao.getItemWhenYearAndMonthSet(year, month)
    }

    fun getAmountWhenYearAndMonthSet(year: Int, month: Int) : List<Int> {
        return itemDao.getAmountWhenYearAndMonthSet(year, month)
    }

    fun getAmountSumOfEachCategory(year: Int) : List<SumOfCategory> {
        return itemDao.getAmountSumOfEachCategory(year)
    }

    fun getCategorySumOfEachMonth(year: Int, category: String) : List<MonthlyCategory> {
        return itemDao.getCategorySumOfEachMonth(year, category)
    }
}