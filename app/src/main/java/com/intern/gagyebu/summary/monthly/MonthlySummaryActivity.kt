package com.intern.gagyebu.summary.monthly

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.intern.gagyebu.R
import com.intern.gagyebu.databinding.ActivityMonthlySummaryBinding
import com.intern.gagyebu.room.ItemRepository
import com.intern.gagyebu.summary.util.BaseActivity

class MonthlySummaryActivity : BaseActivity<ActivityMonthlySummaryBinding>(
    R.layout.activity_monthly_summary
) {
    override val viewModel: ViewModel by lazy {
        ViewModelProvider(
            this, MonthlySummaryViewModel.MonthlySummaryViewModelFactory(ItemRepository(itemDao))
        ).get(MonthlySummaryViewModel::class.java)
    }

    override fun initViewModel(viewModel: ViewModel) {
        binding.lifecycleOwner = this@MonthlySummaryActivity
        binding.viewModel = this@MonthlySummaryActivity.viewModel as MonthlySummaryViewModel
    }

    override fun onCreateAction() {

    }

}