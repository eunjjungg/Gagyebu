package com.intern.gagyebu.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.intern.gagyebu.App
import com.intern.gagyebu.FilterSelectDialog
import com.intern.gagyebu.YearMonthPickerDialog
import com.intern.gagyebu.databinding.ActivityMainBinding
import com.intern.gagyebu.room.AppDatabase
import com.intern.gagyebu.room.ItemRepo

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val database = AppDatabase.getDatabase(App.context())
    private val factory = MainViewModelFactory(ItemRepo(database.itemDao()))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        viewModel.initValue()

        viewModel.incomeValue.observe(this) {
            binding.income.text = it.toString()
        }

        viewModel.spendValue.observe(this) {
            binding.spend.text = it.toString()
        }

        binding.id.setOnClickListener{
            val datePicker = YearMonthPickerDialog()
            datePicker.setListener { _, year, month, _ ->
                Log.d("YearMonthPickerTest", year.toString() + month.toString())
            }
            datePicker.show(supportFragmentManager, "DatePicker")
        }

        binding.spend.setOnClickListener{
            val picker = FilterSelectDialog()
            picker.show(supportFragmentManager, "DatePicker")
        }

        binding.recyclerview.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val adapter = Adapter()
        binding.recyclerview.adapter = adapter
        subscribeUi(adapter, viewModel)
    }

    private fun subscribeUi(adapter: Adapter, viewModel: MainViewModel) {
        viewModel.itemFlow.observe(this) { value ->
            adapter.submitList(value)
        }
    }
}

class MainViewModelFactory(
    private val repository: ItemRepo
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = MainViewModel(repository) as T
}