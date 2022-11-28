package com.intern.gagyebu.summary.monthly

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.intern.gagyebu.room.ItemRepository
import com.intern.gagyebu.summary.util.CategoryInfoOfMonth
import com.intern.gagyebu.summary.util.PieElement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MonthlySummaryViewModel(private val itemRepository: ItemRepository): ViewModel() {
    val pieChartData = MutableLiveData<MutableList<PieElement>>(mutableListOf<PieElement>(
        PieElement("", 0f)
    ))

    private suspend fun getDataFromRepository(year: Int, month: Int): List<CategoryInfoOfMonth> {
        var rawData = listOf<CategoryInfoOfMonth>()
        val job = viewModelScope.async {
            withContext(Dispatchers.IO) {
                val result = itemRepository.getCategoryAndSumWhenYearAndMonthSet(year, month)
                result.sortedByDescending { it.sum }
                rawData = result
            }
        }
        job.await()
        return rawData
    }

    private fun applyDataToPieElement(data: List<CategoryInfoOfMonth>) {
        val _data = if(data.size > 5)
            data.subList(0, 5)
        else data
        var sumOfEachAccount = 0
        for(i in _data.indices)
            sumOfEachAccount += _data[i].sum

        val pieElementList = mutableListOf<PieElement>()
        for(i in _data.indices)
            pieElementList.add(
                PieElement(_data[i].category, _data[i].sum.toFloat() / sumOfEachAccount)
            )

        var checkOne: Float = 0f
        for(i in pieElementList.indices)
            checkOne += pieElementList[i].percentage

        if(checkOne != 1f) {
            val tmp = pieElementList[pieElementList.lastIndex]
            pieElementList.apply {
                removeAt(pieElementList.lastIndex)
                add(PieElement(tmp.name, tmp.percentage + (1f - checkOne)))
            }

        }
        pieChartData.value = pieElementList
    }

    fun getMonthlyReportData(year: Int, month: Int) {
        viewModelScope.launch {
            val data = getDataFromRepository(year, month)
            applyDataToPieElement(data)
        }
    }


    class MonthlySummaryViewModelFactory(private val repository: ItemRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return MonthlySummaryViewModel(repository) as T
        }
    }
}