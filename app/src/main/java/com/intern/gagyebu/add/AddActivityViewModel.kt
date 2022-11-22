package com.intern.gagyebu.add

import android.view.View
import android.widget.AdapterView
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import com.intern.gagyebu.room.ItemEntity
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class AddActivityViewModel : ViewModel() {

    private val category = ObservableField<String>()

    var title = MutableLiveData<String>()
    var amount = MutableLiveData<String>()

    private var year = 0
    private var month = 0
    private var day = 0

    private val _eventFlow = MutableSharedFlow<Event>()

    val eventFlow = _eventFlow.asSharedFlow()

    val enableSave = combine(
        title.asFlow(),
        amount.asFlow(),
    ) { title, amount -> title.isNotBlank() && amount.isNotBlank()}.onStart {
        emit(
            false
        )
    }.asLiveData()

    fun onSelectItem(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        if (parent != null) {
            category.set("${parent.selectedItem}")
        }
    }

    fun selectDate(year: Int, month: Int, day: Int) {
        this.year = year
        this.month = month
        this.day = day
    }

    fun save(view: View) {
        try {
            val amount = amount.value?.let { Integer.parseInt(it) }
            val title = title.value
            val item = ItemEntity(
                amount = amount!!,
                title = title!!,
                year = year,
                month = month,
                day = day,
                category = category.get()!!
            )
            event(Event.Save(item))

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
        data class Save(val value: ItemEntity) : Event()
    }
}