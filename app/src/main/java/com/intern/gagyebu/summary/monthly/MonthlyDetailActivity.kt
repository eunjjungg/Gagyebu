package com.intern.gagyebu.summary.monthly

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.intern.gagyebu.R
import com.intern.gagyebu.databinding.ActivityMonthlyDetailBinding
import com.intern.gagyebu.room.ItemRepo
import com.intern.gagyebu.summary.util.BaseActivity

class MonthlyDetailActivity : BaseActivity<ActivityMonthlyDetailBinding>(R.layout.activity_monthly_detail) {
    override val viewModel by lazy {
        ViewModelProvider(
            this, MonthlySummaryViewModel.MonthlySummaryViewModelFactory(ItemRepo.ItemRepository(itemDao))
        ).get(MonthlySummaryViewModel::class.java)
    }
    override fun initViewModel(viewModel: ViewModel) {
        binding.lifecycleOwner = this@MonthlyDetailActivity
        binding.viewModel = this@MonthlyDetailActivity.viewModel
    }

    override fun onCreateAction() {
        TODO("Not yet implemented")
    }

}