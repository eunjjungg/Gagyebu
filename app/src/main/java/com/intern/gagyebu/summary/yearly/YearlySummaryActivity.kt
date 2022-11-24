package com.intern.gagyebu.summary.yearly

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.intern.gagyebu.R
import com.intern.gagyebu.databinding.ActivityYearlySummaryBinding
import com.intern.gagyebu.room.ItemRepository
import com.intern.gagyebu.summary.util.BaseActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class YearlySummaryActivity() : BaseActivity<ActivityYearlySummaryBinding>(
    R.layout.activity_yearly_summary
) {
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
        setObserver()
    }

    private fun setObserver() {
        this.viewModel.titleYear.observe(this@YearlySummaryActivity, Observer {
            viewModel.getYearReportData()
        })

        this.viewModel.barChartData.observe(this@YearlySummaryActivity, Observer {
            Log.d("ccheck", "dataChanged")
            Log.d("ccheck bar", viewModel.barChartData.value.toString())
        })

        this.viewModel.reportViewData.observe(this@YearlySummaryActivity, Observer {
            Log.d("ccheck", "report data changed")
            Log.d("ccheck report", viewModel.reportViewData.value.toString())
        })
    }

}