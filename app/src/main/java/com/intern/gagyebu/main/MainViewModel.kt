package com.intern.gagyebu.main

import androidx.databinding.ObservableField
import androidx.lifecycle.*
import com.intern.gagyebu.App
import com.intern.gagyebu.room.data.ItemGetOption
import com.intern.gagyebu.dialog.SelectableOptionsEnum
import com.intern.gagyebu.room.ItemEntity
import com.intern.gagyebu.room.ItemRepo
import com.intern.gagyebu.room.data.OptionState
import kotlinx.coroutines.flow.*

/** mainActivity ViewModel**/

class MainViewModel internal constructor(private val itemRepository: ItemRepo) :
    ViewModel() {

    // datastore 객체 초기화
    private val dataStore = OptionState(App.context())

    /**
     * 가저와야 하는 아이템의 종류와 옵션, dataStore에 저장된 값을 가져와 초기화
     * 사용자가 마지막에 저장한 년도,
     *  달,
     *  필터,
     *  옵션
     *  값을 받아와 ItemGetOption (DataClass) 생성
     */
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

    //상단 캘린더의 값을 지정해주는 변수
    var calendarView = ObservableField<String>()

    /**
     * 사용자의 날짜, 옵션 변화로 인해 새로운 값 적용이 필요한 경우 (itemGetOption 의 값이 변화하는 경우)
     * flatMapLatest 을 통해 기존 변환로직 cancel 후 새로운 flow 받아 적용
     * 변화 전파범위 금액 총계, 수입, 지출, 사용자 item
     * flatMap, flatMapFirst, flatMapLatest 각 특징 확인후 적합한 로직 사용.
     */

    //해당 달 수입 춍합
    val incomeValue: LiveData<Int> = itemGetOption.flatMapLatest {
        itemRepository.totalIncome(it)
    }.asLiveData()

    //해당 달 지출 춍합
    val spendValue: LiveData<Int> = itemGetOption.flatMapLatest {
        itemRepository.totalSpend(it)
    }.asLiveData()

    //해당 달 수입 - 지출 값
    val totalValue = combine(
        incomeValue.asFlow(),
        spendValue.asFlow()
    ) { income, spend ->
        income - spend
    }.asLiveData()

    //사용자 item
    val itemFlow: LiveData<List<ItemEntity>> = itemGetOption.flatMapLatest {
        itemRepository.itemGet(it)
    }.asLiveData()

    //필터링 상태인지
    val filterState: LiveData<Boolean> = combine(
        dataStore.filterFlow,
        dataStore.orderFlow
    ) { filter, order ->
        filter != SelectableOptionsEnum.DEFAULT.toString() || order != SelectableOptionsEnum.day.toString()
    }.asLiveData()

    //test 날짜
    val date: LiveData<List<Int>> = combine(
        dataStore.yearFlow,
        dataStore.monthFlow
    ) { year, month -> arrayListOf(year,month)
    }.asLiveData()

}