package com.intern.gagyebu.main

import androidx.databinding.ObservableField
import androidx.lifecycle.*
import com.intern.gagyebu.room.data.ItemGetOption
import com.intern.gagyebu.dialog.Options
import com.intern.gagyebu.room.ItemEntity
import com.intern.gagyebu.room.ItemRepo
import kotlinx.coroutines.flow.*
import java.util.*

class MainViewModel internal constructor(private val itemRepository: ItemRepo):
    ViewModel() {

    private val calendar: Calendar = Calendar.getInstance()

    private val itemGetOption = ItemGetOption(
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH) + 1,
        Options.DEFAULT.toString(),
        Options.day.toString()
    )

    var calendarView = ObservableField<String>()

    private var _queryData = MutableStateFlow(itemGetOption)

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

    fun changeData (year : Int, month:Int){
        _queryData.value = itemGetOption.copy(year = year, month = month)
    }

    fun changeOption (filter : String, order:String){
        _queryData.value = itemGetOption.copy(filter = filter, order = order)
    }

}