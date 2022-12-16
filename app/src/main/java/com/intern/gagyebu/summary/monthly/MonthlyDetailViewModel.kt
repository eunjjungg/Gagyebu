package com.intern.gagyebu.summary.monthly

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

/**
 * 코루틴 제외 리팩토링 하지 않은 클래스
 */

class MonthlyDetailViewModel(private val itemRepository: ItemRepo.ItemRepository) : ViewModel() {
    var dateInfo = DateInfo(0, 0)
    val topCostDetailList = MutableLiveData(mutableListOf<MonthlyDetailInfo>())

    fun setDate(date: DateInfo) {
        dateInfo = date
    }


    // TODO : coroutine 고치기
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


    /*// coroutine 고치기 이전
    fun setTopCostCategory(categoryList: MutableList<PieElement>) {
        pieElementList = categoryList
        viewModelScope.launch {
            getTopCostItemList()
        }
    }

    // coroutine 고치기 이전
    private suspend fun getTopCostItemList() {
        viewModelScope.async {
            withContext(Dispatchers.IO) {
                val tmp = mutableListOf<MonthlyDetailInfo>()
                for (pieElement in pieElementList) {
                    val item = itemRepository.getTopCostItem(dateInfo.year, dateInfo.month, pieElement.name)
                    tmp.add(MonthlyDetailInfo(item, pieElement.percentage.toPercentageInt()))
                }
                withContext(Dispatchers.Main){ topCostDetailList.value = tmp }
            }
        }.await()
    }*/

    private fun Float.toPercentageInt(): Int = Math.round(this * 100)


    class MonthlyDetailViewModelFactory(private val repository: ItemRepo.ItemRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return MonthlyDetailViewModel(repository) as T
        }
    }
}
