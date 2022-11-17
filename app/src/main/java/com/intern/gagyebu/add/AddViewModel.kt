package com.intern.gagyebu.add

import android.view.View
import android.widget.AdapterView
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.intern.gagyebu.room.ItemEntity
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class AddViewModel: ViewModel() {

    var dateString = ObservableField<String>("date")

    var inputYear = ObservableField<Int>()
    var inputMonth = ObservableField<Int>()
    var inputDay = ObservableField<Int>()

    val inputAmount = ObservableField<String>()
    val inputTitle = ObservableField<String>()

    private val _category = ObservableField<String>()

    private val _eventFlow = MutableSharedFlow<Event>()
    val eventFlow = _eventFlow.asSharedFlow()

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
            val amount = inputAmount.get()?.let { Integer.valueOf(it) }
            val title = inputTitle.get()
            val category = _category.get()
            val year = inputYear.get()
            val month = inputMonth.get()
            val day = inputDay.get()

            val item = ItemEntity(amount!!, title!!, year!!, month!!, day!!, category!!)

            event(Event.Save(item))
        }catch (e: NullPointerException){
            event(Event.Error("입력을 확인해주세요"))
        }
    }

    private fun event(event: Event){
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