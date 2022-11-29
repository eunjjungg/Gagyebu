package com.intern.gagyebu.summary.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.intern.gagyebu.room.ItemRepository

open class SampleViewModel(private val itemRepository: ItemRepository): ViewModel() {

    class BaseViewModelFactory(private val repository: ItemRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return SampleViewModel(repository) as T
        }
    }
}