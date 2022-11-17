package com.intern.gagyebu.main

import androidx.lifecycle.LiveData
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

    fun resetOrder(){
        itemListData.value = IsOrder(0)
    }

}