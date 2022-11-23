package com.intern.gagyebu.add

import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import com.intern.gagyebu.App
import com.intern.gagyebu.room.AppDatabase
import com.intern.gagyebu.room.ItemDao
import com.intern.gagyebu.room.ItemEntity
import com.intern.gagyebu.room.ItemRepo
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart

class AddActivityViewModel2 : ViewModel() {

    private var _date: MutableLiveData<String> = MutableLiveData()
    val date: LiveData<String> get() = _date

    private var _title: MutableLiveData<String> = MutableLiveData()
    val title: LiveData<String> get() = _title

    private var _amount: MutableLiveData<String> = MutableLiveData()
    val amount: LiveData<String> get() = _amount

    private var _category: MutableLiveData<String> = MutableLiveData()
    val category: LiveData<String> get() = _category

    fun updateTitle(title: String) {
        _title.value = title
    }

    fun updateAmount(amount: String) {
        _amount.value = amount
    }

    fun updateCategory(category: String) {
        Log.d("opdate", category)
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
    ) { title, amount -> title.isNotBlank() && amount.isNotBlank() }.onStart {
        emit(
            false
        )
    }.asLiveData()

    suspend fun save() {
        val arr = date.value!!.split("-")
        val a: IntArray = arr.map { it.toInt() }.toIntArray()
        try {
            val item = ItemEntity(
                amount = Integer.parseInt(_amount.value)!!,
                title = _title.value!!,
                year = a[0],
                month = a[1],
                day = a[2],
                category = _category.value!!
            )
            val f = AppDatabase.getDatabase(App.context()).itemDao()

            val result: Deferred<String> = CoroutineScope(Dispatchers.IO).async {
                try {
                    withTimeout(2000) {
                        launch {
                            f.saveItem(item)
                        }
                        "저장 완료"
                    }

                } catch (e: TimeoutCancellationException) {
                    "저장 실패"
                }
            }
            event(Event.Done(result.await()))

        } catch (e: NullPointerException) {
            event(Event.Error("입력을 확인해주세요"))
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