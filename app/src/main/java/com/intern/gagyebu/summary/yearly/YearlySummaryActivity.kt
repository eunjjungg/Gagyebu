package com.intern.gagyebu.summary.yearly

import android.content.Intent
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.intern.gagyebu.R
import com.intern.gagyebu.databinding.ActivityYearlySummaryBinding
import com.intern.gagyebu.room.ItemRepository
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
            this, YearlySummaryViewModel.YearlySummaryViewModelFactory(ItemRepository(itemDao))
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

        this.viewModel.isEmpty.observe(this@YearlySummaryActivity, Observer {
            val setVisibilityViewList = mutableListOf(
                binding.rcvBarChart, binding.linearBoxTitle, binding.tvBoxSubTitle,
                binding.boxCustomView, binding.boxBottomLine
            )

            if (viewModel.isEmpty.value!!) {
                resetRecyclerViewAdapter()
                for(view in setVisibilityViewList)
                    view.visibility = View.GONE
            } else {
                for(view in setVisibilityViewList)
                    view.visibility = View.VISIBLE
            }
        })

        this.viewModel.barChartData.observe(this@YearlySummaryActivity, Observer {
            barChartInfoList = viewModel.barChartData.value!!.subList(1, 12)
            resetRecyclerViewAdapter()
        })

        this.viewModel.reportViewData.observe(this@YearlySummaryActivity, Observer {
            binding.boxCustomView.setTextAttribute(viewModel.reportViewData.value as ReportViewInfo)
        })
    }

    private fun resetRecyclerViewAdapter() {
        barChartAdapter.barChartInfo = barChartInfoList
        binding.rcvBarChart.adapter!!.notifyDataSetChanged()
    }

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
                override fun onItemClicked(month: Int) {
                    Intent(this@YearlySummaryActivity, MonthlySummaryActivity::class.java).apply {
                        putExtra("dateInfo", DateInfo(viewModel.titleYear.value!!, month))
                    }.also { startActivity(it) }
                }
            })
        }
    }

}