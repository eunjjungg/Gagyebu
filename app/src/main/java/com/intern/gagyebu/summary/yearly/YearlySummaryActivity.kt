package com.intern.gagyebu.summary.yearly

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.intern.gagyebu.R
import com.intern.gagyebu.databinding.ActivityYearlySummaryBinding
import com.intern.gagyebu.room.ItemRepository
import com.intern.gagyebu.summary.util.BarChartInfo
import com.intern.gagyebu.summary.util.BaseActivity
import com.intern.gagyebu.summary.util.ReportViewInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
            binding.tvBoxTitle.text = String.format(resources.getString(R.string.boxTitle_year), viewModel.titleYear.value)
            binding.tvBoxSubTitle.text = String.format(resources.getString(R.string.boxSubTitle_year), viewModel.titleYear.value)
        })

        this.viewModel.isEmpty.observe(this@YearlySummaryActivity, Observer {
            if(viewModel.isEmpty.value!!) {
                //barChartInfoList =  mutableListOf<BarChartInfo>()
                resetRecyclerViewAdapter()
                binding.rcvBarChart.visibility = View.GONE
                binding.linearBoxTitle.visibility = View.GONE
                binding.tvBoxSubTitle.visibility = View.GONE
                binding.boxCustomView.visibility = View.GONE
                binding.boxBottomLine.visibility = View.GONE
            } else {
                binding.rcvBarChart.visibility = View.VISIBLE
                binding.linearBoxTitle.visibility = View.VISIBLE
                binding.tvBoxSubTitle.visibility = View.VISIBLE
                binding.boxCustomView.visibility = View.VISIBLE
                binding.boxBottomLine.visibility = View.VISIBLE
            }
        })

        this.viewModel.barChartData.observe(this@YearlySummaryActivity, Observer {
            barChartInfoList = viewModel.barChartData.value!!.subList(1, 12)
            resetRecyclerViewAdapter()
        })

        this.viewModel.reportViewData.observe(this@YearlySummaryActivity, Observer {
            Log.d("ccheck report", viewModel.reportViewData.value.toString())
            binding.boxCustomView.setTextAttribute(viewModel.reportViewData.value as ReportViewInfo)
        })
    }

    private fun resetRecyclerViewAdapter() {
        barChartAdapter.barChartInfo = barChartInfoList
        binding.rcvBarChart.adapter!!.notifyDataSetChanged()
    }

    private fun setRecyclerView() {
        barChartAdapter.barChartInfo = barChartInfoList
        binding.rcvBarChart.apply {
            layoutManager = LinearLayoutManager(
                this@YearlySummaryActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = barChartAdapter
        }
    }

}