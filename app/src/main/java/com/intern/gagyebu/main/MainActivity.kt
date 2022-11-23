package com.intern.gagyebu.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.intern.gagyebu.*
import com.intern.gagyebu.add.AddItemActivity
import com.intern.gagyebu.add.AddItemActivity2
import com.intern.gagyebu.databinding.ActivityMainBinding
import com.intern.gagyebu.dialog.OptionDialogListener
import com.intern.gagyebu.dialog.OptionSelectDialog
import com.intern.gagyebu.dialog.YearMonthPickerDialog
import com.intern.gagyebu.room.AppDatabase
import com.intern.gagyebu.room.ItemRepo
import java.util.*

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val database = AppDatabase.getDatabase(App.context())
    private val factory = MainViewModelFactory(ItemRepo(database.itemDao()))

    companion object {
        private val calendar: Calendar = Calendar.getInstance()
        val YEAR = calendar.get(Calendar.YEAR)
        val MONTH = calendar.get(Calendar.MONTH) + 1
        val DATE = calendar.get(Calendar.DATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]
        binding.mainViewModel = viewModel

        binding.calender.text = getString(R.string.show_date, YEAR, MONTH)

        //해당 년/월의 수입 observing
        viewModel.incomeValue.observe(this) {
            binding.income.text = getString(R.string.show_won, "$it")
        }

        //해당 년/월의 지출 observing
        viewModel.spendValue.observe(this) {
            binding.spend.text = getString(R.string.show_won, "$it")
        }

        //해당 년/월의 총합 observing
        viewModel.totalValue.observe(this) {
            binding.total.text = getString(R.string.show_won, "$it")
        }

        //저장버튼 (test)
        binding.save.setOnClickListener {
            startActivity(Intent(this, AddItemActivity2::class.java))
        }

        //달력 다이얼로그
        binding.calender.setOnClickListener {
            val datePicker = YearMonthPickerDialog()
            datePicker.setListener { _, year, month, _ ->
                viewModel.changeData(year, month)
                binding.calender.text = getString(R.string.show_date, year, month)
            }
            datePicker.show(supportFragmentManager, "DatePicker")
        }

        //옵션 다이얼로그
        binding.filter.setOnClickListener {
            val optionPicker = OptionSelectDialog()
            optionPicker.setListener(object : OptionDialogListener {
                override fun option(filter: String, order: String) {
                    viewModel.changeOption(filter, order)
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

