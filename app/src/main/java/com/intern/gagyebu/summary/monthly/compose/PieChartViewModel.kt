package com.intern.gagyebu.summary.monthly.compose

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.intern.gagyebu.room.ItemRepo
import com.intern.gagyebu.summary.util.DateInfo
import com.intern.gagyebu.summary.util.MonthlyDetailInfo
import com.intern.gagyebu.summary.util.PieElement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PieChartViewModel(private val itemRepository: ItemRepo.ItemRepository) : ViewModel() {
    lateinit var dateInfo: DateInfo
    val topCostDetailList = MutableLiveData(mutableListOf<MonthlyDetailInfo>())

    fun setDate(date: DateInfo) {
        dateInfo = date
    }

    fun setTopCostCategory(categoryList: MutableList<PieElement>) {
        viewModelScope.launch {
            topCostDetailList.value = getTopCostItemList(categoryList)
        }
    }

    // TODO : coroutine 고치기
    private suspend fun getTopCostItemList(categoryList: MutableList<PieElement>): MutableList<MonthlyDetailInfo> =
        withContext(Dispatchers.IO) {
            val detailInfo = mutableListOf<MonthlyDetailInfo>()
            for (pieElement in categoryList) {
                detailInfo.add(
                    MonthlyDetailInfo(
                        itemRepository.getTopCostItem(dateInfo.year, dateInfo.month, pieElement.name),
                        pieElement.percentage.toPercentageInt()
                    )
                )
            }
            detailInfo
        }

    private fun Float.toPercentageInt(): Int = Math.round(this * 100)

    class PieChartViewModelFactory(private val repository: ItemRepo.ItemRepository) :
            ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return PieChartViewModel(repository) as T
        }
    }
}