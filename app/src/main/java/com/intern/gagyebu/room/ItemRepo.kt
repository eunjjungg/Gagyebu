package com.intern.gagyebu.room

import android.util.Log
import kotlinx.coroutines.flow.Flow

class ItemRepo constructor(
private val ItemDao: ItemDao
) {

    fun itemFlow(date: Array<Int>): Flow<List<ItemEntity>>{
        return ItemDao.sortDay(date[0], date[1])
    }

    fun totalIncome(date: Array<Int>): Flow<Int>{
        return ItemDao.incomeTotal(date[0], date[1])
    }

    fun totalSpend(date: Array<Int>): Flow<Int>{
        return ItemDao.spendTotal(date[0], date[1])
    }


    fun orderItem(date: Array<Int>,filter:String, sort:String): Flow<List<ItemEntity>> {

        lateinit var item: Flow<List<ItemEntity>>

        item = if (filter == "income"){
            ItemDao.sortInIncome(date[0], date[1], sort)
        }else{
            ItemDao.sortInSpend(date[0], date[1], sort)
        }

        return item
    }
}