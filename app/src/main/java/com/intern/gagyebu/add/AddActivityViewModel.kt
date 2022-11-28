package com.intern.gagyebu.add

import android.content.Intent
import androidx.lifecycle.*
import com.intern.gagyebu.room.ItemEntity
import com.intern.gagyebu.room.ItemRepo
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class AddActivityViewModel : ViewModel() {

    private var _date: MutableLiveData<String> = MutableLiveData()
    val date: LiveData<String> get() = _date

    private var _title: MutableLiveData<String> = MutableLiveData()
    val title: LiveData<String> get() = _title

    private var _amount: MutableLiveData<String> = MutableLiveData()
    val amount: LiveData<String> get() = _amount

    private var _category: MutableLiveData<String> = MutableLiveData("수입")
    val category: LiveData<String> get() = _category

    var activityTitle: String = "항목 저장"

    var itemId: Int = 0

    private var isSaving: MutableLiveData<Boolean> = MutableLiveData(false)

    fun updateTitle(title: String) {
        _title.value = title
    }

    fun updateAmount(amount: String) {
        _amount.value = amount
    }

    fun updateCategory(category: String) {
        _category.value = category
    }

    fun updateDate(date: String) {
        _date.value = date
    }

    private val _eventFlow = MutableSharedFlow<Event>()

    val eventFlow = _eventFlow.asSharedFlow()

    var areInputsValid = combine(
        title.asFlow(),
        amount.asFlow(),
        date.asFlow(),
        isSaving.asFlow()
    ) { title, amount, date, isSaving -> title.isNotBlank() && amount.isNotBlank() && date.isNotBlank() && isSaving == false }.onStart {
        emit(
            false
        )
    }.asLiveData()

    fun saveData() {
        try {
            val dateArr =
                date.value?.split("-")?.map { it.toInt() }?.toIntArray()
                    ?: throw java.lang.IllegalArgumentException("날짜을 확인해주세요")

            val title = title.value?.trim()
                ?: throw java.lang.IllegalArgumentException("제목을 확인해주세요")

            val amount = amount.value?.let {
                if (it[0] == '0' || it.length > 8) {
                    throw java.lang.IllegalArgumentException("금액을 확인해주세요")
                } else {
                    Integer.parseInt(it)
                }

            } ?: throw java.lang.IllegalArgumentException("금액을 확인해주세요")
            val category =
                category.value ?: throw java.lang.IllegalArgumentException("카테고리를 확인해주세요")

            if (activityTitle == "항목 저장") {
                val itemEntity = ItemEntity(
                    id = 0,
                    amount = amount,
                    title = title,
                    year = dateArr[0],
                    month = dateArr[1],
                    day = dateArr[2],
                    category = category
                )
                saveData(itemEntity)

            } else {
                val itemEntity = ItemEntity(
                    id = itemId,
                    amount = amount,
                    title = title,
                    year = dateArr[0],
                    month = dateArr[1],
                    day = dateArr[2],
                    category = category
                )

                updateData(itemEntity)
            }


        } catch (e: IllegalArgumentException) {
            e.message?.let { event(Event.Error(it)) } ?: event(Event.Error("오류가 발생하였습니다."))
        }
    }

    private fun saveData(itemEntity: ItemEntity) {
        isSaving.value = true
        CoroutineScope(Dispatchers.IO).launch {
            try {
                withTimeout(5000) {
                    launch {
                        //delay(4000)
                        ItemRepo.saveItem(itemEntity)
                        event(Event.Done("저장 완료"))
                    }
                }

            } catch (e: TimeoutCancellationException) {
                isSaving.value = false
                event(Event.Error("저장 실패"))
            }
        }
    }

    private fun updateData(itemEntity: ItemEntity) {
        isSaving.value = true
        CoroutineScope(Dispatchers.IO).launch {
            try {
                withTimeout(2000) {
                    launch {
                        ItemRepo.updateItem(itemEntity)
                        event(Event.Done("수정 완료"))
                    }
                }

            } catch (e: TimeoutCancellationException) {
                isSaving.value = false
                event(Event.Error("수정 실패"))
            }
        }
    }


    private fun event(event: Event) {
        viewModelScope.launch {
            _eventFlow.emit(event)
        }
    }

    fun initUpdate(intent: Intent) {
        _date.value = intent.getStringExtra("DATE")
        _title.value = intent.getStringExtra("TITLE")
        _amount.value = intent.getIntExtra("AMOUNT", 0).toString()
        _category.value = intent.getStringExtra("CATEGORY")
        activityTitle = "항목 수정"
        itemId = intent.getIntExtra("ID", 0)
    }

    sealed class Event {
        data class Error(val value: String) : Event()
        data class Done(val value: String) : Event()
    }
}