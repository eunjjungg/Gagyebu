package com.intern.gagyebu.summary.monthly

import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.intern.gagyebu.R
import com.intern.gagyebu.databinding.ActivityMonthlySummaryBinding
import com.intern.gagyebu.room.ItemRepository
import com.intern.gagyebu.summary.util.BaseActivity
import com.intern.gagyebu.summary.util.DateInfo
import com.intern.gagyebu.summary.yearly.YearlySummaryViewModel.Companion.months

class MonthlySummaryActivity : BaseActivity<ActivityMonthlySummaryBinding>(
    R.layout.activity_monthly_summary
) {
    override val viewModel by lazy {
        ViewModelProvider(
            this, MonthlySummaryViewModel.MonthlySummaryViewModelFactory(ItemRepository(itemDao))
        ).get(MonthlySummaryViewModel::class.java)
    }
    var year: Int = 0
    var month: Int = 0
    val descList: MutableList<TextView> = mutableListOf()

    override fun initViewModel(viewModel: ViewModel) {
        binding.lifecycleOwner = this@MonthlySummaryActivity
        binding.viewModel = this@MonthlySummaryActivity.viewModel
    }

    //이 액티비티의 onCreate()에서 처리할 부분
    override fun onCreateAction() {
        getParcel()
        binding.setView()
        setObserver()
        viewModel.getMonthlyReportData(year, month)
    }

    //이전 액티비티(연간 요약)로부터 Parcel로 사용자가 누른 연, 월의 정보를 받음
    //이를 클래스 내 글로벌 변수인 year, month에 각각 할당해주는 함수
    private fun getParcel() {
        val bundle = intent.extras
        val dateInfo: DateInfo? = bundle?.getParcelable("dateInfo")
        year = dateInfo?.year ?: 0
        month = dateInfo?.month ?: 0
    }

    //todo: 뷰모델의 코루틴에서 결과값을 받아서 이걸 뷰로 던져서 이벤트 처리?
    //pie chart를 그릴 데이터인 pieChartData LiveData를 뷰에서 observe하고
    //값이 변경될 때마다 (이 액티비티가 새로 그려질 때마다) pieChart를 그리도록 함.
    //또한 각 파이차트의 원소들의 설명을 나타낸 descList: MutableList<TextView>도
    //pie Chart에 그려진 원소의 개수만큼 visible을 나머지는 gone으로 설정.
    private fun setObserver() {
        this.viewModel.pieChartData.observe(this@MonthlySummaryActivity, Observer {
            binding.pieChart.setPercentage(viewModel.pieChartData.value!!)
            var tmp = 0
            for (i in viewModel.pieChartData.value!!.indices) {
                descList[i].text = viewModel.pieChartData.value!![i].name
                descList[i].visibility = View.VISIBLE
                tmp = i
            }
            for (i in tmp + 1..descList.size - 1) {
                descList[i].visibility = View.GONE
            }
        })
    }

    //title은 현재 월로 바꿔주고 위 setObserver에서 사용하는 textView 리스트인
    //descList 정의해줌.
    private fun ActivityMonthlySummaryBinding.setView() {
        title.text = String.format(
            resources.getString(R.string.pieTitle_month),
            months[month]
        )
        descList.apply {
            add(pieDesc0)
            add(pieDesc1)
            add(pieDesc2)
            add(pieDesc3)
            add(pieDesc4)
        }
    }
}