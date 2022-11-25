package com.intern.gagyebu.summary.monthly

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.intern.gagyebu.room.ItemRepository

class MonthlySummaryViewModel(private val itemRepository: ItemRepository): ViewModel() {

    class MonthlySummaryViewModelFactory(private val repository: ItemRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return MonthlySummaryViewModel(repository) as T
        }
    }
}