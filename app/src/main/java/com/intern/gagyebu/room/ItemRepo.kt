package com.intern.gagyebu.room

import com.intern.gagyebu.App
import com.intern.gagyebu.ItemGetOption
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
        val item = if (data.filter == "all"){
            ItemDao.sortDay(data.year, data.month, data.order)
        } else if (data.filter == "spend") {
            ItemDao.sortInSpend(data.year, data.month, data.order)
        } else {
            ItemDao.sortInIncome(data.year, data.month, data.order)
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
}

    private fun <T: Int?> Flow<T?>.filterNull(): Flow<T> = transform { value ->
        if (value != null){
            return@transform emit(value)
        }else{
            return@transform emit(0 as T)
        }
    }
