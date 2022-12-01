package com.intern.gagyebu.summary.monthly

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.intern.gagyebu.room.ItemRepo
import com.intern.gagyebu.summary.util.DateInfo
import com.intern.gagyebu.summary.util.MonthlyDetailInfo
import com.intern.gagyebu.summary.util.PieElement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MonthlyDetailViewModel(private val itemRepository: ItemRepo.ItemRepository) : ViewModel() {
    var dateInfo = DateInfo(0, 0)
    val topCostItemList = mutableListOf<MonthlyDetailInfo>()
    var topCostCategoryList = mutableListOf<PieElement>()

    fun setDate(date: DateInfo) {
        dateInfo = date
    }

    fun setTopCostCategory(categoryList: MutableList<PieElement>) {
        topCostCategoryList = categoryList
        viewModelScope.launch {
            getTopCostItemList()
        }
    }

    private suspend fun getTopCostItemList() {
        viewModelScope.async {
            withContext(Dispatchers.IO) {

            }
        }.await()
    }

    class MonthlyDetailViewModelFactory(private val repository: ItemRepo.ItemRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return MonthlyDetailViewModel(repository) as T
        }
    }
}