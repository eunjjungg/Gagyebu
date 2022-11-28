package com.intern.gagyebu.summary.monthly

import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.get
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.intern.gagyebu.R
import com.intern.gagyebu.databinding.ActivityMonthlySummaryBinding
import com.intern.gagyebu.room.ItemRepository
import com.intern.gagyebu.summary.util.BaseActivity
import com.intern.gagyebu.summary.util.DateInfo
import com.intern.gagyebu.summary.util.PieElement
import com.intern.gagyebu.summary.yearly.YearlySummaryViewModel
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

    override fun onCreateAction() {
        getParcel()
        binding.setView()
        setObserver()
        viewModel.getMonthlyReportData(year, month)
    }

    private fun getParcel() {
        val bundle = intent.extras
        val dateInfo: DateInfo? = bundle?.getParcelable("dateInfo")
        year = dateInfo?.year ?: 0
        month = dateInfo?.month ?: 0
    }

    private fun setObserver() {
        this.viewModel.pieChartData.observe(this@MonthlySummaryActivity, Observer {
            binding.pieChart.setPercentage(viewModel.pieChartData.value!!)
            var tmp = 0
            for(i in viewModel.pieChartData.value!!.indices) {
                descList[i].text = viewModel.pieChartData.value!![i].name
                descList[i].visibility = View.VISIBLE
                tmp = i
            }
            for(i in tmp + 1..descList.size - 1) {
                descList[i].visibility = View.GONE
            }
        })
    }

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