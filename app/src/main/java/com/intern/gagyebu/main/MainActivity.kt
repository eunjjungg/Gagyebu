package com.intern.gagyebu.main

import android.animation.ObjectAnimator
import android.content.Intent
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.compose.material3.MaterialTheme
import android.util.TypedValue
import android.view.animation.OvershootInterpolator
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.intern.gagyebu.*
import com.intern.gagyebu.produce.ProduceActivity
import com.intern.gagyebu.databinding.ActivityMainBinding
import com.intern.gagyebu.dialog.OptionDialogListener
import com.intern.gagyebu.dialog.OptionSelectDialog
import com.intern.gagyebu.dialog.YearMonthPickerDialog
import com.intern.gagyebu.room.ItemRepo
import com.intern.gagyebu.room.data.OptionState
import com.intern.gagyebu.summary.yearly.YearlySummaryActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

/** mainActivity **/

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val factory = MainViewModelFactory(ItemRepo)
    private val dataStore = OptionState(App.context())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //viewModel, 데이터 초기화
        val viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]
        binding.mainViewModel = viewModel

        //recyclerView 초기화
        val adapter = Adapter()
        binding.recyclerview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerview.adapter = adapter
        subscribeUi(adapter, viewModel)

        //compose migration
        binding.composeView.setContent {
            MaterialTheme() {
                MonthlyDescription(viewModel)
            }
        }

        //필터링 상태에 따라 icon 색상 변경
        viewModel.filterState.observe(this){
            when(it){
                "nomal" -> binding.filter.imageTintList = ColorStateList.valueOf(android.graphics.Color.parseColor("#000000"))

                else -> binding.filter.imageTintList = ColorStateList.valueOf(android.graphics.Color.parseColor("#E74141"))
            }
        }

        viewModel.date.observe(this){
            viewModel.calendarView.set(getString(R.string.show_date, it[0], it[1]))
        }

        /**달력 다이얼로그
         * 사용자가 입력한 DATE 가 Listener 를 통해 전달되면 값 DataStore 에 저장
         */
        binding.calender.setOnClickListener {
            val datePicker = YearMonthPickerDialog()
            datePicker.setListener { _, year, month, _ ->
                CoroutineScope(Dispatchers.Main).launch {
                    dataStore.setYear(year)
                    dataStore.setMonth(month)
                }
            }
            datePicker.show(supportFragmentManager, "DatePicker")
        }

        //필터링, 정렬 다이얼로그
        binding.filter.setOnClickListener {
            val optionPicker = OptionSelectDialog()
            optionPicker.setListener(object : OptionDialogListener {
                override fun option(filter: String, order: String) {
                    //필터 변경시 아이템 스크롤 최상단으로 이동.
                    binding.recyclerview.smoothScrollToPosition(0)
                }
            })
            optionPicker.show(supportFragmentManager, "OptionPicker")
        }
        setFabClickListener()
    }

    //adapter diff observe 등록
    //ViewModel 에서 받은 ItemList 변경점 diffUtil observe
    private fun subscribeUi(adapter: Adapter, viewModel: MainViewModel) {
        viewModel.itemFlow.observe(this) { value ->
            adapter.submitList(value){
            }
        }
    }

    private var isFabOpen = false

    private fun setFabClickListener() {
        binding.fabDefault.setOnClickListener {
            openFab()
        }
        binding.fabAdd.setOnClickListener {
            Intent(this@MainActivity, ProduceActivity::class.java).also { startActivity(it) }
        }
        binding.fabCalendar.setOnClickListener {
            Intent(this@MainActivity, YearlySummaryActivity::class.java).also { startActivity(it) }
        }
    }

    private fun openFab() {
        val targetPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50f, this.resources.displayMetrics)
        val setAnimator = {button: FloatingActionButton, startF: Float, endF: Float, duration: Long ->
            ObjectAnimator.ofFloat(button, "translationY", startF, endF).apply {
                interpolator = OvershootInterpolator()
                this.duration = duration
                start()
            }
        }
        if(isFabOpen) {
            setAnimator(binding.fabAdd, -targetPx, 0f, 400L)
            setAnimator(binding.fabCalendar, -targetPx * 2, 0f, 500L)
        }
        else {
            setAnimator(binding.fabAdd, 0f, -targetPx, 400L)
            setAnimator(binding.fabCalendar, 0f, -targetPx * 2, 500L)
        }
        isFabOpen = !isFabOpen
    }

    companion object {
        private val calendar: Calendar = Calendar.getInstance()
        val YEAR = calendar.get(Calendar.YEAR)
        val MONTH = calendar.get(Calendar.MONTH) + 1
    }

}

class MainViewModelFactory(
    private val repository: ItemRepo
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = MainViewModel(repository) as T
}



