package com.intern.gagyebu.summary.yearly

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.intern.gagyebu.R
import com.intern.gagyebu.databinding.ActivityYearlySummaryBinding
import com.intern.gagyebu.room.ItemRepo
import com.intern.gagyebu.summary.monthly.MonthlySummaryActivity
import com.intern.gagyebu.summary.util.BarChartInfo
import com.intern.gagyebu.summary.util.BaseActivity
import com.intern.gagyebu.summary.util.DateInfo
import com.intern.gagyebu.summary.util.ReportViewInfo

class YearlySummaryActivity() : BaseActivity<ActivityYearlySummaryBinding>(
    R.layout.activity_yearly_summary
) {
    var barChartInfoList = mutableListOf<BarChartInfo>()
    val barChartAdapter by lazy { BarChartAdapter() }
    override val viewModel by lazy {
        ViewModelProvider(
            this, YearlySummaryViewModel.YearlySummaryViewModelFactory(ItemRepo.ItemRepository(itemDao), application = application)
        ).get(YearlySummaryViewModel::class.java)
    }

    override fun initViewModel(viewModel: ViewModel) {
        binding.lifecycleOwner = this@YearlySummaryActivity
        binding.viewModel = this@YearlySummaryActivity.viewModel
    }

    override fun onCreateAction() {
        setRecyclerView()
        setObserver()
    }

    private fun setObserver() {
        //사용자가 연도를 이동하였을 때 : 연도 title를 바꿔주고 Data를 DB로부터 새로 가져오도록 함수 호출함
        this.viewModel.titleYear.observe(this@YearlySummaryActivity, Observer {
            viewModel.getYearReportData()
            binding.tvBoxTitle.text = String.format(
                resources.getString(R.string.boxTitle_year),
                viewModel.titleYear.value
            )
            binding.tvBoxSubTitle.text = String.format(
                resources.getString(R.string.boxSubTitle_year),
                viewModel.titleYear.value
            )
        })

        //연도를 이동하여 데이터를 가져왔지만 데이터가 없는 경우에는 필요 없는 부분이 보이지 않도록
        //가시성을 변경해주고 데이터가 있는 경우에는 모든 뷰가 보이도록 설정해줌
        this.viewModel.isEmpty.observe(this@YearlySummaryActivity, Observer {
            val setVisibilityViewList = mutableListOf(
                binding.rcvBarChart, binding.linearBoxTitle, binding.tvBoxSubTitle,
                binding.boxCustomView, binding.boxBottomLine
            )

            //isEmpty.value에는 무조건 true, false 둘 중 하나가 들어가므로 !! 연산자 사용
            if (viewModel.isEmpty.value!!) {
                for (view in setVisibilityViewList)
                    view.visibility = View.GONE
            } else {
                for (view in setVisibilityViewList)
                    view.visibility = View.VISIBLE
            }
        })

        //연간 데이터를 다 담은 barChartData는 값이 변화하게 되면 연간 막대 그래프를 다시 그려야 함.
        //따라서 데이터를 바꿔주고 notifyDataSetChanged()를 해줌.
        //총 12개의 데이터의 전체가 바뀌는 것이므로 notifyDataSetChanged() 사용.
        this.viewModel.barChartData.observe(this@YearlySummaryActivity, Observer {
            barChartInfoList = viewModel.barChartData.value!!.subList(
                1, viewModel.barChartData.value!!.size
            )

            resetRecyclerViewAdapter()
        })

        //텍스트뷰에 들어가는 내용이 달라지게 될 때도 다시 그려줘야 함
        this.viewModel.reportViewData.observe(this@YearlySummaryActivity, Observer {
            binding.boxCustomView.setTextAttribute(viewModel.reportViewData.value as ReportViewInfo)
        })

        //사용자 입장에서 연도를 바꾸게 되면 -> viewModel.titleYear가 바뀌고
        //이 변화를 뷰에서 옵저빙해서 data를 가져오도록 하고
        //해당 데이터가 어떻냐에 따라 viewModel.isEmpty, barChartData, reportViewData를 각각 변경시켜주고
        //이 값의 변화를 또 뷰에서 옵저빙해서 그에 해당하는 행동을 해주는 구조.
    }

    //recyclerView 갱신
    private fun resetRecyclerViewAdapter() {
        barChartAdapter.barChartInfo = barChartInfoList
        binding.rcvBarChart.adapter!!.notifyDataSetChanged()
    }

    //Init RecyclerView
    //각 리사이클러뷰의 원소에 대해 클릭 리스너도 여기서 달아줌.
    private fun setRecyclerView() {
        binding.rcvBarChart.apply {
            layoutManager = LinearLayoutManager(
                this@YearlySummaryActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = barChartAdapter
        }

        barChartAdapter.apply {
            barChartInfo = barChartInfoList
            setOnItemClickListener(object : BarChartClickListener {
                override fun onItemClicked(item: BarChartInfo) {
                    if(item.percentage > 0f){
                        Intent(
                            this@YearlySummaryActivity,
                            MonthlySummaryActivity::class.java
                        ).apply {
                            putExtra("dateInfo", DateInfo(viewModel.titleYear.value!!, item.month))
                        }.also { startActivity(it) }
                    }
                }
            })
        }
    }

}