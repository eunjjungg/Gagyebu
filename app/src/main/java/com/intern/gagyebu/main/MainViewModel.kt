package com.intern.gagyebu.main

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import com.intern.gagyebu.ItemGetOption
import com.intern.gagyebu.room.ItemEntity
import com.intern.gagyebu.room.ItemRepo
import kotlinx.coroutines.flow.*

class MainViewModel internal constructor(private val itemRepository: ItemRepo):
    ViewModel() {

    private var _queryData = MutableStateFlow<ItemGetOption>(ItemGetOption(0,0,"null", "null"))
    private var _date = ObservableField<String>()
    var date = ObservableField<String>()
    get() = _date

    val incomeValue: LiveData<Int> = _queryData.flatMapLatest {
        itemRepository.totalIncome(it)
    }.asLiveData()

    val spendValue: LiveData<Int> = _queryData.flatMapLatest {
        itemRepository.totalSpend(it)
    }.asLiveData()

    val totalValue = combine(
        incomeValue.asFlow(),
        spendValue.asFlow()
    )  { income, spend -> income - spend
         }.asLiveData()

    val itemFlow: LiveData<List<ItemEntity>> = _queryData.flatMapLatest {
        itemRepository.itemGet(it)
    }.asLiveData()

    fun setData(data: ItemGetOption) {
        _queryData.value = data.copy()
        _date.set("${data.year}" + "년" + "${data.month}" + "월")
        Log.d("gi", _date.get().toString())
    }


}