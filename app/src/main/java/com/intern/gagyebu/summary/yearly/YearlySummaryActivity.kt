package com.intern.gagyebu.summary.yearly

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.intern.gagyebu.R
import com.intern.gagyebu.databinding.ActivityYearlySummaryBinding
import com.intern.gagyebu.room.ItemRepository
import com.intern.gagyebu.summary.util.BarChartInfo
import com.intern.gagyebu.summary.util.BaseActivity
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
        })

        this.viewModel.barChartData.observe(this@YearlySummaryActivity, Observer {
            barChartInfoList = viewModel.barChartData.value!!.subList(1, 12)
            barChartAdapter.barChartInfo = barChartInfoList
            binding.rcvBarChart.adapter!!.notifyDataSetChanged()
        })

        this.viewModel.reportViewData.observe(this@YearlySummaryActivity, Observer {
            Log.d("ccheck report", viewModel.reportViewData.value.toString())
        })
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