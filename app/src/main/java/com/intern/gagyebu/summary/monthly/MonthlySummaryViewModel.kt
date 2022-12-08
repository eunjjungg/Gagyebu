package com.intern.gagyebu.summary.monthly

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.intern.gagyebu.room.ItemRepo
import com.intern.gagyebu.summary.util.CategoryInfoOfMonth
import com.intern.gagyebu.summary.util.PieElement
import kotlinx.coroutines.*

class MonthlySummaryViewModel(private val itemRepository: ItemRepo.ItemRepository) : ViewModel() {
    //view에서 파이차트를 그리기 위한 각 pieElement live data
    val pieChartData = MutableLiveData<MutableList<PieElement>>()
    val pieElementList = mutableListOf<PieElement>()

    // TODO : coroutine 고치기 일단은 완료
    //직접 db에 접근해서 데이터를 가져오는 함수
    /*fun getMonthlyReportData(year: Int, month: Int){
        viewModelScope.launch(Dispatchers.IO) {
            val result = itemRepository.getCategoryAndSumWhenYearAndMonthSet(year, month)
            result.sortedByDescending { it.sum }

            withContext(Dispatchers.Main) {
                pieChartData.value = applyDataToPieElement(result)
            }
        }
    }*/

    //직접 db에 접근해서 데이터를 가져오는 함수 - 2번째 구현 방법
    fun getMonthlyReportData(year: Int, month: Int) =
        viewModelScope.launch {
            pieChartData.value = applyDataToPieElement(getDataFromRepository(year, month).sortedByDescending { it.sum })
        }

    private suspend fun getDataFromRepository(year: Int, month: Int): List<CategoryInfoOfMonth> =
        withContext(Dispatchers.IO) {
            itemRepository.getCategoryAndSumWhenYearAndMonthSet(year, month)
        }

    //db에서 가져온 데이터를 pieElement에 맞도록 보정해주는 부분
    private fun applyDataToPieElement(data: List<CategoryInfoOfMonth>) : MutableList<PieElement> {
        val _data = if (data.size > 5) {
            data.subList(0, 5)
        }
        else {
            data
        }

        val pieElementList = mutableListOf<PieElement>()

        var sumOfEachAccount = 0

        for (i in _data.indices)
            sumOfEachAccount += _data[i].sum

        //각 부분이 차지하는 %를 구하여 PieElement에 할당
        //소숫점 셋째 자리 이하 버리기 사용
        for (i in _data.indices)
            pieElementList.add(
                PieElement(
                    _data[i].category,
                    Math.round(_data[i].sum.toFloat() / sumOfEachAccount * 100) / 100f
                )
            )

        //소숫점 셋째 자리 이하 버리기를 사용했기에
        //각 합이 총 1(100%)을 이루는지 확인
        var checkOne: Float = 0f
        for (i in pieElementList.indices)
            checkOne += pieElementList[i].percentage

        if (checkOne != 1f) {
            val tmp = pieElementList[pieElementList.lastIndex]
            pieElementList.apply {
                removeAt(pieElementList.lastIndex)
                add(PieElement(tmp.name, tmp.percentage + (1f - checkOne)))
            }
        }
        var check = 0f
        for (i in 0..pieElementList.size - 1) {
            check += pieElementList[i].percentage
        }

        return pieElementList
    }

    class MonthlySummaryViewModelFactory(private val repository: ItemRepo.ItemRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return MonthlySummaryViewModel(repository) as T
        }
    }
}

