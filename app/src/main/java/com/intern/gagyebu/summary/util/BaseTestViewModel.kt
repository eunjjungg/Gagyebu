package com.intern.gagyebu.summary.util

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.intern.gagyebu.room.ItemEntity
import com.intern.gagyebu.room.ItemRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BaseTestViewModel(private val itemRepository: ItemRepository): ViewModel() {
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

    class BaseTestViewModelFactory(private val itemRepository: ItemRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(BaseTestViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return BaseTestViewModel(itemRepository) as T
            }
            throw java.lang.IllegalArgumentException("this is not proper ViewModel")
        }
    }
}