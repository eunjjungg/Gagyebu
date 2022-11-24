package com.intern.gagyebu.add

import android.util.Log
import androidx.lifecycle.*
import com.intern.gagyebu.App
import com.intern.gagyebu.room.AppDatabase
import com.intern.gagyebu.room.ItemEntity
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart

class AddActivityViewModel : ViewModel() {

    private var _date: MutableLiveData<String> = MutableLiveData()
    val date: LiveData<String> get() = _date

    private var _title: MutableLiveData<String> = MutableLiveData()
    val title: LiveData<String> get() = _title

    private var _amount: MutableLiveData<String> = MutableLiveData()
    val amount: LiveData<String> get() = _amount

    private var _category: MutableLiveData<String> = MutableLiveData("수입")
    val category: LiveData<String> get() = _category

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

    val areInputsValid = combine(
        title.asFlow(),
        amount.asFlow(),
        date.asFlow()
    ) { title, amount, date -> title.isNotBlank() && amount.isNotBlank() && date.isNotBlank() }.onStart {
        emit(
            false
        )
    }.asLiveData()

    suspend fun save() {
        try {
            val dateArr =
                date.value?.let { date -> date.split("-").map { it.toInt() }.toIntArray() }
                    ?: throw java.lang.IllegalArgumentException("날짜을 확인해주세요")

            val title = title.value?.let { it.trim() }
                ?: throw java.lang.IllegalArgumentException("제목을 확인해주세요")
            val amount = amount.value?.let {
                if (it[0] == '0' || it.length > 8) {
                    throw java.lang.IllegalArgumentException("금액을 확인해주세요")
                } else {
                    it
                }
            } ?: throw java.lang.IllegalArgumentException("금액을 확인해주세요")
            val category =
                category.value ?: throw java.lang.IllegalArgumentException("카테고리를 확인해주세요")

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    withTimeout(2000) {
                        launch {
                            AppDatabase.getDatabase(App.context()).itemDao().saveItem(
                                ItemEntity(
                                    amount = Integer.parseInt(amount),
                                    title = title,
                                    year = dateArr[0],
                                    month = dateArr[1],
                                    day = dateArr[2],
                                    category = category
                                )
                            )
                            event(Event.Done("저장 완료"))
                        }
                    }

                } catch (e: TimeoutCancellationException) {
                    event(Event.Error("저장 실패"))
                }
            }


        } catch (e: IllegalArgumentException) {
            e.message?.let { event(Event.Error(it)) } ?: event(Event.Error("오류가 발생하였습니다."))
        }
    }

    private fun event(event: Event) {
        viewModelScope.launch {
            _eventFlow.emit(event)
        }
    }

    sealed class Event {
        data class Error(val value: String) : Event()
        data class Done(val value: String) : Event()
    }
}