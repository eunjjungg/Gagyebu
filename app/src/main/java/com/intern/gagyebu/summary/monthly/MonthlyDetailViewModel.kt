package com.intern.gagyebu.summary.monthly

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.intern.gagyebu.room.ItemEntity
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
    val topCostDetailList = mutableListOf<MonthlyDetailInfo>()
    var pieElementList = mutableListOf<PieElement>()

    fun setDate(date: DateInfo) {
        dateInfo = date
    }

    fun setTopCostCategory(categoryList: MutableList<PieElement>) {
        pieElementList = categoryList
        viewModelScope.launch {
            getTopCostItemList()
        }
    }

    private suspend fun getTopCostItemList() {
        viewModelScope.async {
            withContext(Dispatchers.IO) {
                for (pieElement in pieElementList) {
                    val item = itemRepository.getTopCostItem(dateInfo.year, dateInfo.month, pieElement.name)
                    topCostDetailList.add(MonthlyDetailInfo(item, pieElement.percentage.toPercentageInt()))
                }
            }
        }.await()
    }

    private fun Float.toPercentageInt(): Int = Math.round(this * 100)


    class MonthlyDetailViewModelFactory(private val repository: ItemRepo.ItemRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return MonthlyDetailViewModel(repository) as T
        }
    }
}