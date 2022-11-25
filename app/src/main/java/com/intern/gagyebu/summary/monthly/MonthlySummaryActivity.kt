package com.intern.gagyebu.summary.monthly

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.intern.gagyebu.R
import com.intern.gagyebu.databinding.ActivityMonthlySummaryBinding
import com.intern.gagyebu.room.ItemRepository
import com.intern.gagyebu.summary.util.BaseActivity
import com.intern.gagyebu.summary.util.DateInfo
import com.intern.gagyebu.summary.yearly.YearlySummaryViewModel

class MonthlySummaryActivity : BaseActivity<ActivityMonthlySummaryBinding>(
    R.layout.activity_monthly_summary
) {
    override val viewModel by lazy {
        ViewModelProvider(
            this, MonthlySummaryViewModel.MonthlySummaryViewModelFactory(ItemRepository(itemDao))
        ).get(MonthlySummaryViewModel::class.java)
    }

    override fun initViewModel(viewModel: ViewModel) {
        binding.lifecycleOwner = this@MonthlySummaryActivity
        binding.viewModel = this@MonthlySummaryActivity.viewModel
    }

    override fun onCreateAction() {
        getParcel()
    }

    private fun getParcel() {
        val bundle = intent.extras
        val dateInfo: DateInfo? = bundle?.getParcelable("dateInfo")
        Toast.makeText(this, dateInfo.toString(), Toast.LENGTH_SHORT).show()
    }
}