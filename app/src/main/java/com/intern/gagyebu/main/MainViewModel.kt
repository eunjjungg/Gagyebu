package com.intern.gagyebu.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import androidx.lifecycle.asLiveData
import com.intern.gagyebu.room.ItemEntity
import com.intern.gagyebu.room.ItemRepo
import java.util.Calendar

class MainViewModel internal constructor(private val itemRepository: ItemRepo):
    ViewModel() {
    private val calendar: Calendar = Calendar.getInstance()

    //test
    private var _date = MutableStateFlow<Array<Int>>(arrayOf(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1))
    private var _isFiltering = MutableStateFlow<String>("none")
    private var _isOrder = MutableStateFlow<String>("none")

    val incomeValue: LiveData<Int> = _date.flatMapLatest {
        itemRepository.totalIncome(it)
    }.asLiveData()

    val spendValue: LiveData<Int> = _date.flatMapLatest {
        itemRepository.totalSpend(it)
    }.asLiveData()


    val itemFlow: LiveData<List<ItemEntity>> = _date.flatMapLatest {
        if(_isFiltering.value == "none" && _isOrder.value == "none"){
            itemRepository.itemFlow(_date.value)
        }else{
            itemRepository.orderItem(_date.value, _isFiltering.value, _isOrder.value)
        }
    }.asLiveData()


    fun setDate(value: Array<Int>) {
        _date.value = value
    }

    fun setFilter(value: String) {
        _isFiltering.value = value
    }

    fun setOrder(value: String) {
        _isOrder.value = value
    }


}