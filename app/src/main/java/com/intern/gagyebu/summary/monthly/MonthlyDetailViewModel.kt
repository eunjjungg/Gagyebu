package com.intern.gagyebu.summary.monthly

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.intern.gagyebu.room.ItemRepo
import com.intern.gagyebu.summary.util.MonthlyDetailInfo
import com.intern.gagyebu.summary.util.PieElement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MonthlyDetailViewModel(private val itemRepository: ItemRepo.ItemRepository) : ViewModel() {
    val topCostItemList = mutableListOf<MonthlyDetailInfo>()
    var topCostCategoryList = mutableListOf<PieElement>()

    //fun getTopCostItemList
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