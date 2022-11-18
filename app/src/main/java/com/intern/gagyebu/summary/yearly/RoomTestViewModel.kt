package com.intern.gagyebu.summary.yearly

import android.util.Log
import android.view.View
import androidx.lifecycle.*
import com.intern.gagyebu.room.ItemEntity
import com.intern.gagyebu.room.ItemRepository
import kotlinx.coroutines.*

class RoomTestViewModel(private val itemRepository: ItemRepository) : ViewModel() {
    fun getItemList() {
        Log.d("item list", "onclicked")
        var tmp: List<ItemEntity>

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                tmp = itemRepository.getItemsWhenYearAndMonthSet(2022, 11)
                Log.d("item list", tmp.toString())
            }
        }

    }

    class RoomTestViewModelFactory(private val itemRepository: ItemRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RoomTestViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return RoomTestViewModel(itemRepository) as T
            }
            throw java.lang.IllegalArgumentException("this is not proper ViewModel")
        }
    }

}
