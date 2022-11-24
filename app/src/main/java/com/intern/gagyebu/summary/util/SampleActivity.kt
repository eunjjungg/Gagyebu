package com.intern.gagyebu.summary.util

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.intern.gagyebu.R
import com.intern.gagyebu.databinding.ActivityBaseTestBinding
import com.intern.gagyebu.room.ItemRepository

class SampleActivity() : BaseActivity<ActivityBaseTestBinding>(R.layout.activity_base_test) {
    override val viewModel by lazy {
        ViewModelProvider(
            this, SampleViewModel.BaseViewModelFactory(ItemRepository(itemDao = itemDao))
        ).get(SampleViewModel::class.java)
    }

    override fun initViewModel(viewModel: ViewModel) {
        binding.viewModel = this.viewModel
        binding.lifecycleOwner = this@SampleActivity
    }

    override fun onCreateAction() {
        //onCreate actions code
    }


}