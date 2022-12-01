package com.intern.gagyebu.summary.util

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.intern.gagyebu.room.AppDatabase
import com.intern.gagyebu.room.ItemRepo.ItemRepository

abstract class BaseActivity<VD: ViewDataBinding>(resource: Int) : AppCompatActivity() {
    val a = 0

    /*@LayoutRes
    abstract fun getLayoutRes(): Int*/

    abstract val viewModel: ViewModel

    val binding by lazy {
        DataBindingUtil.setContentView(this, resource) as VD
    }

    val itemDao by lazy {
        AppDatabase.getDatabase(this).itemDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding
        super.onCreate(savedInstanceState)
        initViewModel(viewModel)
        onCreateAction()
    }

    abstract fun initViewModel(viewModel: ViewModel)
    abstract fun onCreateAction()
}