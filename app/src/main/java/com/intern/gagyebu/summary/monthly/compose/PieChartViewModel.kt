package com.intern.gagyebu.summary.monthly.compose

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.intern.gagyebu.room.ItemEntity
import com.intern.gagyebu.room.ItemRepo
import com.intern.gagyebu.summary.util.CategoryInfoOfMonth
import com.intern.gagyebu.summary.util.DateInfo
import com.intern.gagyebu.summary.util.MonthlyDetailInfoWithState
import com.intern.gagyebu.summary.util.PieChartData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PieChartViewModel(private val itemRepository: ItemRepo.ItemRepository) : ViewModel() {
    var sliceThickness by mutableStateOf(25f)

    var pieChartData by mutableStateOf(
        PieChartData(
            slices = mutableListOf()
        )
    )

    var cardData by mutableStateOf(
        mutableListOf<MonthlyDetailInfoWithState>()
    )

    fun getCardList(dateInfo: DateInfo) {
        viewModelScope.launch {
            setDataToCardData(getCardListFromDB(dateInfo).toMutableList())
        }
    }

    private fun setDataToCardData(
        itemEntityList: MutableList<ItemEntity>,
    ) {
        val list: MutableList<MonthlyDetailInfoWithState> = mutableListOf()
        itemEntityList.forEachIndexed() { index, itemEntity ->
            list.add(
                MonthlyDetailInfoWithState(
                    itemEntity,
                    index + 1
                )
            )
        }
        cardData = list
    }

    private suspend fun getCardListFromDB(dateInfo: DateInfo): List<ItemEntity> =
        withContext(Dispatchers.IO) {
            itemRepository.run {
                val itemEntityList = mutableListOf<ItemEntity>()
                getCategoryAndSumWhenYearAndMonthSet(
                    dateInfo.year,
                    dateInfo.month
                ).forEach { category ->
                    itemEntityList.add(
                        getTopCostItem(
                            dateInfo.year,
                            dateInfo.month,
                            category.category
                        )
                    )
                }
                itemEntityList
            }
        }

    fun getTopCostList(dateInfo: DateInfo) {
        viewModelScope.launch {
            pieChartData.slices = setDataToSlice(getTopCostListFromDB(dateInfo).toMutableList())
        }
    }

    private fun setDataToSlice(rawData: MutableList<CategoryInfoOfMonth>): List<PieChartData.Slice> {
        val list: MutableList<PieChartData.Slice> = mutableListOf()
        rawData.forEach {
            list.add(PieChartData.Slice(it.sum.toFloat(), it.category))
        }
        return list
    }

    private suspend fun getTopCostListFromDB(dateInfo: DateInfo): List<CategoryInfoOfMonth> =
        withContext(Dispatchers.IO) {
            itemRepository.getCategoryAndSumWhenYearAndMonthSet(dateInfo.year, dateInfo.month)
        }

    class PieChartViewModelFactory(private val repository: ItemRepo.ItemRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return PieChartViewModel(repository) as T
        }
    }
}