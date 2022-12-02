package com.intern.gagyebu.dialog

import android.app.AlertDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.os.Bundle
import android.widget.NumberPicker
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.intern.gagyebu.App
import com.intern.gagyebu.databinding.YearMonthPickerBinding
import com.intern.gagyebu.main.MainActivity.Companion.MONTH
import com.intern.gagyebu.main.MainActivity.Companion.YEAR
import com.intern.gagyebu.room.data.OptionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

/** 날짜 변경을 위한 YearMonthPickerDialog **/

class YearMonthPickerDialog : DialogFragment() {

    private lateinit var customListener: OnDateSetListener

    fun setListener(listener: OnDateSetListener?) {
        if (listener != null) {
            this.customListener = listener
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val binding = YearMonthPickerBinding.inflate(requireActivity().layoutInflater)
        val dataStore = OptionState(App.context())

        val monthPicker = binding.pickerMonth
        val yearPicker = binding.pickerYear

        monthPicker.minValue = 1
        monthPicker.maxValue = 12

        yearPicker.minValue = MIN_YEAR
        yearPicker.maxValue = MAX_YEAR

        /**
         * Dialog 시작 전, 년도, 월 값을 사용자가 마지막으로 입력한 값으로 초기화
         */
        CoroutineScope(Dispatchers.Main).launch{
            yearPicker.value  = dataStore.yearFlow.first()
            monthPicker.value = dataStore.monthFlow.first()
        }

        binding.cancel.setOnClickListener {
            this@YearMonthPickerDialog.dialog?.cancel()
        }

        //confirm 버튼 클릭시 년도와 월 전달.
        binding.confirm.setOnClickListener {
            customListener.onDateSet(null, yearPicker.value, monthPicker.value, 0)
            this@YearMonthPickerDialog.dialog?.cancel()
        }

        builder.setView(binding.root)

        return builder.create()
    }

    companion object {
        private const val MAX_YEAR = 2099
        private const val MIN_YEAR = 1980
    }

}
