package com.intern.gagyebu.room

import android.util.Log
import kotlinx.coroutines.flow.Flow

class ItemRepo constructor(
private val ItemDao: ItemDao
) {

    val itemFlow: Flow<List<ItemEntity>>
        get() = ItemDao.sortDay(10)


    fun orderItem(isOrder: IsOrder): Flow<List<ItemEntity>> {

        val item = when(isOrder){
            sortInSpend -> {
                Log.d("item", "spend")
                ItemDao.sortInSpend(10, "amount")
            }

            sortInIncome -> {
                Log.d("item", "income")
                ItemDao.sortInIncome(10, "amount")
            }
            else -> {ItemDao.sortDay(10)}
        }
        return item
    }
}