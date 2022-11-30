package com.intern.gagyebu.main

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import com.intern.gagyebu.App
import com.intern.gagyebu.room.data.ItemGetOption
import com.intern.gagyebu.dialog.Options
import com.intern.gagyebu.room.ItemEntity
import com.intern.gagyebu.room.ItemRepo
import com.intern.gagyebu.room.data.OptionState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.*

/** mainActivity ViewModel**/

class MainViewModel internal constructor(private val itemRepository: ItemRepo) :
    ViewModel() {

    private val dataStore = OptionState(App.context())

    private val itemGetOption: Flow<ItemGetOption> = combine(
        dataStore.yearFlow,
        dataStore.monthFlow,
        dataStore.filterFlow,
        dataStore.orderFlow
    ) { year, month, filter, order ->
        ItemGetOption(
            year,
            month,
            filter,
            order
        )
    }

    var calendarView = ObservableField<String>()

    var filter = OptionState(App.context()).filterFlow

    val incomeValue: LiveData<Int> = itemGetOption.flatMapLatest {
        itemRepository.totalIncome(it)
    }.asLiveData()

    val spendValue: LiveData<Int> = itemGetOption.flatMapLatest {
        itemRepository.totalSpend(it)
    }.asLiveData()

    val totalValue = combine(
        incomeValue.asFlow(),
        spendValue.asFlow()
    ) { income, spend ->
        income - spend
    }.asLiveData()

    val itemFlow: LiveData<List<ItemEntity>> = itemGetOption.flatMapLatest {
        itemRepository.itemGet(it)
    }.asLiveData()

    //필터링 상태인지
    val filterState: LiveData<String> = combine(
        dataStore.filterFlow,
        dataStore.orderFlow
    ) { filter, order ->
        if (filter != Options.DEFAULT.toString() || order != Options.day.toString()) {
            "not_nomal"
        } else {
            "nomal"
        }
    }.asLiveData()

    //test 날짜
    val date: LiveData<List<Int>> = combine(
        dataStore.yearFlow,
        dataStore.monthFlow
    ) { year, month -> arrayListOf(year,month)
    }.asLiveData()

}