package com.intern.gagyebu.summary.monthly

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.intern.gagyebu.R
import com.intern.gagyebu.databinding.ActivityMonthlyDetailBinding
import com.intern.gagyebu.main.MonthlyDescription
import com.intern.gagyebu.room.ItemRepo
import com.intern.gagyebu.summary.util.BaseActivity
import com.intern.gagyebu.summary.util.DateInfo
import com.intern.gagyebu.summary.util.PieElement

class MonthlyDetailActivity : BaseActivity<ActivityMonthlyDetailBinding>(R.layout.activity_monthly_detail) {
    override val viewModel by lazy {
        ViewModelProvider(
            this, MonthlyDetailViewModel.MonthlyDetailViewModelFactory(ItemRepo.ItemRepository(itemDao))
        ).get(MonthlyDetailViewModel::class.java)
    }
    override fun initViewModel(viewModel: ViewModel) {
        binding.lifecycleOwner = this@MonthlyDetailActivity
        binding.viewModel = this@MonthlyDetailActivity.viewModel
    }

    override fun onCreateAction() {
        getParcel()
        setComposeView()
    }

    private fun setComposeView() {
        binding.composeView.setContent {
            MaterialTheme() {
                MonthlySummaryCompose(viewModel)
            }
        }
    }

    private fun getParcel() {
        val bundle = intent.extras

        val dateInfo: DateInfo? = bundle?.getParcelable("dateInfo")
        if(dateInfo != null) {
            viewModel.setDate(dateInfo)
        }

        val elementInfoList = mutableListOf<PieElement>()
        val elementInfoSize: Int = bundle?.getInt("elementInfoSize") ?: 0
        for(i in 0..elementInfoSize - 1) {
            val tmp: PieElement? = bundle?.getParcelable("elementInfo$i")
            if(tmp != null)
                elementInfoList.add(tmp)
        }
        viewModel.setTopCostCategory(elementInfoList)
    }

}