package com.intern.gagyebu.room

import com.intern.gagyebu.App
import com.intern.gagyebu.room.data.ItemGetOption
import com.intern.gagyebu.summary.util.CategoryInfoOfMonth
import com.intern.gagyebu.summary.util.MonthlyCategory
import com.intern.gagyebu.summary.util.SumOfCategory
import com.intern.gagyebu.dialog.SelectableOptionsEnum
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

object ItemRepo {

    private val ItemDao = AppDatabase.getDatabase(App.context()).itemDao()

    fun totalIncome(data: ItemGetOption): Flow<Int> {
        return ItemDao.incomeTotal(data.year, data.month).filterNull()
    }

    fun totalSpend(data: ItemGetOption): Flow<Int>{
        return ItemDao.spendTotal(data.year, data.month).filterNull()
    }

    fun itemGet(data: ItemGetOption): Flow<List<ItemEntity>> {
        val item = when (data.filter) {
            SelectableOptionsEnum.DEFAULT.toString() -> {
                ItemDao.sortDay(data.year, data.month, data.order)
            }
            SelectableOptionsEnum.SPEND.toString() -> {
                ItemDao.sortInSpend(data.year, data.month, data.order)
            }
            else -> {
                ItemDao.sortInIncome(data.year, data.month, data.order)
            }
        }
        return item
    }

    fun deleteItem(id: Int){
        CoroutineScope(Dispatchers.IO).launch {
            ItemDao.deleteItem(id)
        }
    }

    fun saveItem(itemEntity: ItemEntity){
        CoroutineScope(Dispatchers.IO).launch {
            ItemDao.saveItem(itemEntity)
        }
    }

    fun updateItem(itemEntity: ItemEntity){
        CoroutineScope(Dispatchers.IO).launch {
            ItemDao.updateItem(itemEntity)
        }
    }

    class ItemRepository(private val itemDao: ItemDao) {

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

        fun getTopCostItem(year: Int, month: Int, category: String) : ItemEntity {
            return itemDao.getTopCostItem(year, month, category)
        }
    }
}

    private fun <T: Int?> Flow<T?>.filterNull(): Flow<T> = transform { value ->
        if (value != null){
            return@transform emit(value)
        }else{
            return@transform emit(0 as T)
        }
    }
