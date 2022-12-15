package com.intern.gagyebu.summary.yearly

import android.app.Application
import androidx.lifecycle.*
import com.intern.gagyebu.Comma
import com.intern.gagyebu.R
import com.intern.gagyebu.room.ItemRepo
import com.intern.gagyebu.summary.util.BarChartInfo
import com.intern.gagyebu.summary.util.MonthlyCategory
import com.intern.gagyebu.summary.util.ReportViewInfo
import com.intern.gagyebu.summary.util.SumOfCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat

class YearlySummaryViewModel(
    private val itemRepository: ItemRepo.ItemRepository,
    application: Application
) : AndroidViewModel(application), Comma {

    //월이 함께 들어가는 barChartData, percentageOfEachMonth는 index가 1, 2, 3일 때
    //1, 2, 3월로 사용하기 위해 index가 0일 때 더미 값을 넣어줌
    private var nowYear: Int = 0
    val titleYear = MutableLiveData<Int>(0)


    val isEmpty = MutableLiveData<Boolean>(false)
    val barChartData = MutableLiveData<MutableList<BarChartInfo>>(
        mutableListOf(
            BarChartInfo(0, 0f)
        )
    )
    val reportViewData = MutableLiveData<ReportViewInfo>()

    //뷰모델이 생성되는 처음에만 연도를 현재 연도로 설정해줌.
    init {
        setYearToCurrent()
        for (i in 1..12) {
            barChartData.value!!.add(BarChartInfo(i, 0f))
        }
    }

    //Object로 사용되는 부분
    companion object {
        val months = listOf(
            "none",
            "JAN",
            "FEB",
            "MAR",
            "APR",
            "MAY",
            "JUN",
            "JUL",
            "AUG",
            "SEP",
            "OCT",
            "NOV",
            "DEC"
        )
    }

    //liveData - titleYear를 현재 연도로 설정해주는 함수
    private fun setYearToCurrent() {
        fun convertTimeMillisToCurrentYear(): Int {
            val millis = System.currentTimeMillis()
            val format = SimpleDateFormat("yyyy")
            return format.format(millis).toInt()
        }
        nowYear = convertTimeMillisToCurrentYear()
        titleYear.value = nowYear
    }

    //data binding 함수
    fun increaseYear() {
        titleYear.value = titleYear.value!!.plus(1)
    }

    //data binding 함수
    fun decreaseYear() {
        titleYear.value = titleYear.value!!.minus(1)
    }

    // TODO : coroutine 고치기 일단은 완료 (데이터를 다 받아야만 다른 메소드를 수행할 수 있으므로 현재로서는 코루틴의 이득을 볼 수 없는 구조입니다.)
    //뷰에서 데이터가 바뀐 것을 감지했을 때 불리는 함수
    fun getYearReportData() {
        viewModelScope.launch {
            setBarChartData(getYearMonthAmountData(titleYear.value!!))

            if (isEmpty.value == false) {
                reportViewData.value = getYearCategoryData(titleYear.value!!)
            }
        }
    }

    // TODO : coroutine 고치기 일단은 완료
    //1월부터 12월까지 각 얼마를 소비했는지 DB에 접근해서 데이터를 받아오는 함수
    private suspend fun getYearMonthAmountData(year: Int): MutableList<Int> =
        withContext(Dispatchers.IO) {
            val sumOfMoney = MutableList<Int>(13) { _ -> 0 }
            for (month in 1..12) {
                sumOfMoney[month] = itemRepository.getAmountWhenYearAndMonthSet(year, month).sum()
            }
            sumOfMoney
        }


    private fun setBarChartData(sumOfMoney: MutableList<Int>) {
        //만약 소비 기록이 없다면 세팅해주기 전에 함수 종료
        if (isFullOfZero(sumOfMoney)) {
            isEmpty.value = true
            return
        }

        //소비 기록이 있다면 isEmpty를 false로 설정하고
        //가장 큰 소비인 달에 비해 각각 몇퍼센트인지 계산해주고 이 값을
        //barChartData에 월과 함께 넣어줌.
        isEmpty.value = false

        for (i in 1..sumOfMoney.size - 1) {
            barChartData.value!![i] = BarChartInfo(
                i, Math.round(sumOfMoney[i] / sumOfMoney.max().toFloat() * 100) / 100f
            )
        }

        barChartData.notifyObserver()
    }

    //빈 데이터(널값 대신 0으로 모두 채워진 경우)인지 확인하는 함수
    private fun isFullOfZero(list: List<Int>): Boolean {
        for (i in list) {
            if (i != 0)
                return false
        }
        return true
    }

    // TODO : coroutine 고치기 일단은 완료
    //특정 연도의 카테고리별로 소비한 금액을 모두 가져와서 데이터 정리하는 함수
    private suspend fun getYearCategoryData(year: Int): ReportViewInfo =
        withContext(Dispatchers.IO) {
            val result = itemRepository.getAmountSumOfEachCategory(year)

            //result가 존재하는 경우 최대 항목을 가져오기 위해 maxCategory 설정
            val maxCategory = result.maxBy { it.sum }

            //monthListGroupByMaxCategory is already sorted by descending order
            //해당 연도에 최대로 소비한 카테고리에 대해 월별 상세 항목은 어떤지 가져오는 함수
            val monthListGroupByMaxCategory = itemRepository.getCategorySumOfEachMonth(
                year, maxCategory.category
            )

            convertRawToReportData(maxCategory, monthListGroupByMaxCategory)
        }

    //각 데이터들을 커스텀뷰의 속성에 바로 할당해줄수 있도록 데이터 바꿔주는 부분
    private fun convertRawToReportData(
        maxCategory: SumOfCategory, monthList: List<MonthlyCategory>
    ): ReportViewInfo {
        fun getCostString(index: Int): String =
            String.format(
                getStringResource(R.string.boxTitle_cost),
                months[monthList[index].month],
                monthList[index].sum.addComma()
            )

        val average = String.format(
            getStringResource(R.string.boxTitle_sum),
            maxCategory.sum.addComma()
        )

        val cost2 = if (monthList.size >= 2) {
            getCostString(1)
        } else {
            null
        }

        val cost3 = if (monthList.size >= 3) {
            getCostString(2)
        } else {
            null
        }

        return ReportViewInfo(maxCategory.category, average, getCostString(0), cost2, cost3)
    }

    private fun getStringResource(id: Int): String =
        getApplication<Application>().resources.getString(id)

    //처음 Init부터 총 13개의 List를 만들어줬고 뷰모델의 코드에서는 List의 각 원소의 값만 변경함
    //따라서 그대로만 사용하면 뷰의 옵저버가 감지를 못하므로 값의 변경이 모두 일어난 후
    //라이브데이터.notifyObserver()를 통해 한 번에 옵저빙하여 뷰에서 필요한 사항을 처리하도록 함.
    private fun <T> MutableLiveData<T>.notifyObserver() {
        this.value = this.value
    }

    class YearlySummaryViewModelFactory(
        private val repository: ItemRepo.ItemRepository,
        private val application: Application
    ) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return modelClass.getConstructor(
                ItemRepo.ItemRepository::class.java,
                Application::class.java
            ).newInstance(repository, application)
        }
    }
}