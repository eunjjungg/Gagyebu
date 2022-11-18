package com.intern.gagyebu.main

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import androidx.lifecycle.asLiveData
import com.intern.gagyebu.ItemGetOption
import com.intern.gagyebu.room.ItemEntity
import com.intern.gagyebu.room.ItemRepo
import java.util.Calendar

class MainViewModel internal constructor(private val itemRepository: ItemRepo):
    ViewModel() {

    private var _data = MutableStateFlow<ItemGetOption>(ItemGetOption(0,0,"null", "null"))

    val incomeValue: LiveData<Int> = _data.flatMapLatest {
        itemRepository.totalIncome(it)
    }.asLiveData()

    val spendValue: LiveData<Int> = _data.flatMapLatest {
        itemRepository.totalSpend(it)
    }.asLiveData()

    val itemFlow: LiveData<List<ItemEntity>> = _data.flatMapLatest {
        itemRepository.itemGet(it)
    }.asLiveData()

    fun setData(data: ItemGetOption) {
        _data.value = data.copy()
    }
}