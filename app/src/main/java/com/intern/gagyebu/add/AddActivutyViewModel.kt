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

class AddActivutyViewModel: ViewModel() {

    val _date = MutableLiveData<String>()
    val _title = MutableLiveData<String>()
    val _amount = MutableLiveData<String>()

    val date: LiveData<String>
        get() = _date

    val _category = ObservableField<String>()

    private val _eventFlow = MutableSharedFlow<Event>()
    val eventFlow = _eventFlow.asSharedFlow()

    val enableSave = combine(
        _title.asFlow(),
        _amount.asFlow(),
        _date.asFlow()
    ) { title, amount, date -> title.isNotBlank() && amount.isNotBlank() && date.isNotBlank()}.onStart { emit(false) }.asLiveData()

    fun inputDate(view: View) {
        event(Event.InputDate("date"))
    }

    fun onSelectItem(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        if (parent != null) {
            _category.set("${parent.selectedItem}")
        }
    }

    fun save(view: View) {
        try {
            val amount = _amount.value?.let { Integer.parseInt(it) }
            val title = _title.value
            val date = _date.value?.split("-")

            val year = Integer.parseInt(date!![0])
            val month = Integer.parseInt(date[1])
            val day = Integer.parseInt(date[2])


            val item = ItemEntity(amount = amount!!, title = title!!, year = year, month = month, day = day, _category.toString())
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
        data class InputDate(val value: String) : Event()
        data class Error(val value: String) : Event()
        data class Save(val value: ItemEntity) : Event()
    }
}