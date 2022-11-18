package com.intern.gagyebu.main

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import androidx.lifecycle.asLiveData
import com.intern.gagyebu.room.IsOrder
import com.intern.gagyebu.room.ItemEntity
import com.intern.gagyebu.room.ItemRepo
import com.intern.gagyebu.room.sortDay

class MainViewModel internal constructor(private val itemRepository: ItemRepo):
    ViewModel() {

    private val itemListData = MutableStateFlow<IsOrder>(sortDay)
    private val selectMonth = MutableStateFlow<Int>(10)

    val incomeValue: LiveData<Int> = selectMonth.flatMapLatest {
        itemRepository.totalIncome(it)
    }.asLiveData()

    val spendValue: LiveData<Int> = selectMonth.flatMapLatest {
        itemRepository.totalSpend(it)
    }.asLiveData()

    val itemFlow: LiveData<List<ItemEntity>> = selectMonth.flatMapLatest {
        itemRepository.itemFlow(it)
    }.asLiveData()

    /*
    val itemFlow: LiveData<List<ItemEntity>> = itemListData.flatMapLatest { isorder ->
        if(isorder == sortDay) {
            itemRepository.itemFlow
        }else{
            itemRepository.orderItem(isorder)
        }
    }.asLiveData()

    fun setOrder(value: Int) {
        itemListData.value = IsOrder(value)
    }

     */

    fun setDate(value: Int) {
        selectMonth.value = value
    }

    fun initValue(){
        itemListData.value = IsOrder(0)
    }

}