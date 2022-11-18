package com.intern.gagyebu.room

import com.intern.gagyebu.ItemGetOption
import kotlinx.coroutines.flow.Flow

class ItemRepo constructor(
private val ItemDao: ItemDao
) {

    fun totalIncome(data: ItemGetOption): Flow<Int>{
        return ItemDao.incomeTotal(data.year, data.month)
    }

    fun totalSpend(data: ItemGetOption): Flow<Int>{
        return ItemDao.spendTotal(data.year, data.month)
    }

    fun itemGet(data: ItemGetOption): Flow<List<ItemEntity>> {
        val item = if (data.filter == "all"){
            ItemDao.sortDay(data.year, data.month, data.order)
        } else if (data.filter == "spend") {
            ItemDao.sortInSpend(data.year, data.month, data.order)
        } else {
            ItemDao.sortInIncome(data.year, data.month, data.order)
        }
        return item
    }
}