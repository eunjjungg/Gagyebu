package com.intern.gagyebu.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.intern.gagyebu.App
import com.intern.gagyebu.FilterSelectDialog
import com.intern.gagyebu.YearMonthPickerDialog
import com.intern.gagyebu.databinding.ActivityMainBinding
import com.intern.gagyebu.room.AppDatabase
import com.intern.gagyebu.room.ItemRepo
import java.util.Calendar
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val database = AppDatabase.getDatabase(App.context())
    private val factory = MainViewModelFactory(ItemRepo(database.itemDao()))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        /*
        var monthValue: Int by Delegates.observable(Calendar.YEAR) { property, oldValue, newValue ->
            viewModel.setDate(newValue)
            binding.id.text = "$newValue" + "월 잔고"
        }*/

        viewModel.incomeValue.observe(this) {
            binding.income.text = it.toString()
        }

        viewModel.spendValue.observe(this) {
            binding.spend.text = it.toString()
        }

        //달력 다이얼로그
        binding.id.setOnClickListener {
            val datePicker = YearMonthPickerDialog()
            datePicker.setListener { _, year, month, _ ->
                Log.d("YearMonthPickerTest", year.toString() + month.toString())
                //monthValue = month
                binding.id.text = "$year"+"년"+"$month"+"월"+"잔고"
                viewModel.setDate(arrayOf(year, month))
            }
            datePicker.show(supportFragmentManager, "DatePicker")
        }

        //정렬 다이얼로그
        binding.spend.setOnClickListener {
            val picker = FilterSelectDialog()
            picker.show(supportFragmentManager, "DatePicker")
        }

        //recyclerView init
        binding.recyclerview.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val adapter = Adapter()
        binding.recyclerview.adapter = adapter
        subscribeUi(adapter, viewModel)
    }

    //adapter diff observe 등록
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