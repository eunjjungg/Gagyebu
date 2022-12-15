package com.intern.gagyebu.summary.monthly

import android.content.Intent
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.intern.gagyebu.R
import com.intern.gagyebu.databinding.ActivityMonthlySummaryBinding
import com.intern.gagyebu.room.ItemRepo
import com.intern.gagyebu.summary.monthly.compose.ComposeActivity
import com.intern.gagyebu.summary.util.BaseActivity
import com.intern.gagyebu.summary.util.DateInfo
import com.intern.gagyebu.summary.util.PieElement
import com.intern.gagyebu.summary.yearly.YearlySummaryViewModel.Companion.months

class MonthlySummaryActivity : BaseActivity<ActivityMonthlySummaryBinding>(
    R.layout.activity_monthly_summary
) {
    override val viewModel by lazy {
        ViewModelProvider(
            this, MonthlySummaryViewModel.MonthlySummaryViewModelFactory(ItemRepo.ItemRepository(itemDao))
        ).get(MonthlySummaryViewModel::class.java)
    }
    var year: Int = 0
    var month: Int = 0

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
        setListener()
    }

    //이전 액티비티(연간 요약)로부터 Parcel로 사용자가 누른 연, 월의 정보를 받음
    //이를 클래스 내 글로벌 변수인 year, month에 각각 할당해주는 함수
    private fun getParcel() {
        val bundle = intent.extras
        val dateInfo: DateInfo? = bundle?.getParcelable("dateInfo")
        year = dateInfo?.year ?: 0
        month = dateInfo?.month ?: 0
    }

    //pie chart를 그릴 데이터인 pieChartData LiveData를 뷰에서 observe하고
    //값이 변경될 때마다 (이 액티비티가 새로 그려질 때마다) pieChart를 그리도록 함.
    //또한 각 파이차트의 원소들의 설명을 나타낸 descList: MutableList<TextView>도
    //pie Chart에 그려진 원소의 개수만큼 visible을 나머지는 gone으로 설정.
    private fun setObserver() {
        this.viewModel.pieChartData.observe(this@MonthlySummaryActivity, Observer {
            binding.pieChart.setPercentage(viewModel.pieChartData.value!!)
        })
    }

    //title은 현재 월로 바꿔주고 위 setObserver에서 사용하는 textView 리스트인
    //descList 정의해줌.
    private fun ActivityMonthlySummaryBinding.setView() {
        title.text = String.format(
            resources.getString(R.string.pieTitle_month),
            months[month]
        )
    }

    private fun setListener() {
        binding.btnDetail.setOnClickListener {
            /*Intent(this@MonthlySummaryActivity, MonthlyDetailActivity::class.java)
                .apply {
                    for(i in viewModel.pieChartData.value!!.indices) {
                        putExtra("elementInfo$i", viewModel.pieChartData.value!![i])
                    }
                    putExtra("elementInfoSize", viewModel.pieChartData.value!!.size)
                    putExtra("dateInfo", DateInfo(year, month))
                }
                .also { startActivity(it) }*/

            Intent(this@MonthlySummaryActivity, ComposeActivity::class.java)
                .apply {
                    putExtra("dateInfo", DateInfo(year, month))
                }
                .also { startActivity(it) }
        }
    }


}