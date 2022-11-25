package com.intern.gagyebu.summary.yearly

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.intern.gagyebu.room.ItemRepository
import com.intern.gagyebu.summary.util.BarChartInfo
import com.intern.gagyebu.summary.util.MonthlyCategory
import com.intern.gagyebu.summary.util.ReportViewInfo
import com.intern.gagyebu.summary.util.SumOfCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat

class YearlySummaryViewModel(private val itemRepository: ItemRepository): ViewModel() {
    private var nowYear: Int = 0
    val titleYear = MutableLiveData<Int>(0)
    val sumOfMoney = MutableList<Int>(13, {i -> 0})
    val percentageOfEachMonth = MutableList<Float>(13, {i -> 0f})
    val isEmpty = MutableLiveData<Boolean>(false)
    val barChartData = MutableLiveData<MutableList<BarChartInfo>>(mutableListOf(
        BarChartInfo(0, 0f)
    ))
    val reportViewData = MutableLiveData<ReportViewInfo>()

    init {
        setYearToCurrent()
        for(i in 1..12){
            barChartData.value!!.add(BarChartInfo(i, 0f))
        }
    }

    companion object {
        val months = listOf(
            "none", "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"
        )
    }

    private fun setYearToCurrent() {
        fun convertTimeMillisToCurrentYear(): Int {
            val millis = System.currentTimeMillis()
            val format = SimpleDateFormat("yyyy")
            return format.format(millis).toInt()
        }
        nowYear = convertTimeMillisToCurrentYear()
        titleYear.value = nowYear
    }

    fun increaseYear() {
        titleYear.value = titleYear.value!!.plus(1)
    }

    fun decreaseYear() {
        titleYear.value = titleYear.value!!.minus(1)
    }

    fun getYearReportData() {
        viewModelScope.launch {
            getYearMonthAmountData(titleYear.value!!)
            getYearCategoryData(titleYear.value!!)
        }
    }

    private suspend fun getYearMonthAmountData(year: Int) {
        val job = viewModelScope.async {
            withContext(Dispatchers.IO) {
                for(month in 1..12){
                    val result =
                        itemRepository.getAmountWhenYearAndMonthSet(year, month)
                    sumOfMoney[month] = result.sum()
                }
            }
        }

        job.await()

        if(isFullOfZero(sumOfMoney)) {
            isEmpty.value = true
            return
            //todo empty page 처리 필요?
        }

        for(i in 1..sumOfMoney.size - 1) {
            percentageOfEachMonth[i] = Math.round(sumOfMoney[i] / sumOfMoney.max().toFloat() * 100) / 100f
            barChartData.value!![i] = BarChartInfo(i, percentageOfEachMonth[i])
        }
        barChartData.notifyObserver()
    }

    private fun isFullOfZero(list: List<Int>): Boolean {
        for(i in list) {
            if(i != 0)
                return false
        }
        return true
    }

    private suspend fun getYearCategoryData(year: Int) {
        viewModelScope.async {
            withContext(Dispatchers.IO) {
                val result = itemRepository.getAmountSumOfEachCategory(year)
                if(result.isEmpty()) {
                    //todo : empty page 처리 필요?
                    return@withContext
                }
                val maxCategory = result.maxBy { it.sum }
                //monthListGroupByMaxCategory is already sorted by descending order
                val monthListGroupByMaxCategory = itemRepository.getCategorySumOfEachMonth(
                    year, maxCategory.category
                )

                withContext(Dispatchers.Main){
                    reportViewData.value =
                        convertRawToReportData(maxCategory, monthListGroupByMaxCategory)
                }
            }
        }.await()
    }

    private fun convertRawToReportData(
        maxCategory: SumOfCategory, monthList: List<MonthlyCategory>
    ): ReportViewInfo {
        val theme = "${maxCategory.category}"
        val average = "총 ${maxCategory.sum}원 소비"
        val cost1 = "${months[monthList[0].month]} : ${monthList[0].sum}원"
        var cost2: String? = null
        var cost3: String? = null

        if(monthList.size >= 2) {
            cost2 = "${months[monthList[1].month]} : ${monthList[1].sum}원"
        }
        if(monthList.size >= 3) {
            cost3 = "${months[monthList[2].month]} : ${monthList[2].sum}원"
        }

        return ReportViewInfo(theme, average, cost1, cost2, cost3)
    }

    fun <T> MutableLiveData<T>.notifyObserver() {
        this.value = this.value
    }

    class YearlySummaryViewModelFactory(private val repository: ItemRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return YearlySummaryViewModel(repository) as T
        }
    }
}