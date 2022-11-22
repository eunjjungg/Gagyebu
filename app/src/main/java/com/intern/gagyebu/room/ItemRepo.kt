package com.intern.gagyebu.room

import android.util.Log
import androidx.lifecycle.asLiveData
import com.intern.gagyebu.ItemGetOption
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.*

class ItemRepo constructor(
private val ItemDao: ItemDao
) {

    fun totalIncome(data: ItemGetOption): Flow<Int> {
        return ItemDao.incomeTotal(data.year, data.month).filterNull()
    }

    fun totalSpend(data: ItemGetOption): Flow<Int>{
        return ItemDao.spendTotal(data.year, data.month).filterNull()
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

    private fun <T: Int?> Flow<T?>.filterNull(): Flow<T> = transform { value ->
        if (value != null){
            return@transform emit(value)
        }else{
            return@transform emit(0 as T)
        }
    }

}