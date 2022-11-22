package com.intern.gagyebu.main

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.intern.gagyebu.*
import com.intern.gagyebu.add.AddActivutyViewModel
import com.intern.gagyebu.add.AddItemActivity
import com.intern.gagyebu.databinding.ActivityMainBinding
import com.intern.gagyebu.dialog.OptionDialogListener
import com.intern.gagyebu.dialog.OptionSelectDialog
import com.intern.gagyebu.dialog.YearMonthPickerDialog
import com.intern.gagyebu.room.AppDatabase
import com.intern.gagyebu.room.ItemRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val database = AppDatabase.getDatabase(App.context())
    private val factory = MainViewModelFactory(ItemRepo(database.itemDao()))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]
        binding.mainViewModel = viewModel
        val calendar: Calendar = Calendar.getInstance()

        val itemGetOption = ItemGetOption(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH) + 1,
            "all",
            "date"
        )
        viewModel.setData(itemGetOption)

        //해당 년/월의 수입 observing
        viewModel.incomeValue.observe(this) {
            binding.income.text = it.toString() + " 원"
        }

        //해당 년/월의 지출 observing
        viewModel.spendValue.observe(this) {
            binding.spend.text = it.toString() + " 원"
        }

        //해당 년/월의 총합 observing
        viewModel.totalValue.observe(this) {
            binding.total.text = it.toString() + " 원"
        }

        binding.save.setOnClickListener {
            startActivity(Intent(this, AddItemActivity::class.java))
        }

        //달력 다이얼로그
        binding.id.setOnClickListener {
            val datePicker = YearMonthPickerDialog()
            datePicker.setListener { _, year, month, _ ->
                itemGetOption.year = year
                itemGetOption.month = month
                viewModel.setData(itemGetOption)
            }

            datePicker.show(supportFragmentManager, "DatePicker")
        }

        //옵션 다이얼로그
        binding.filter.setOnClickListener {
            val optionPicker = OptionSelectDialog()
            optionPicker.setListener(object : OptionDialogListener {
                override fun option(filter: String, order: String) {
                    itemGetOption.filter = filter
                    itemGetOption.order = order
                    viewModel.setData(itemGetOption)
                }
            })

            optionPicker.show(supportFragmentManager, "OptionPicker")
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