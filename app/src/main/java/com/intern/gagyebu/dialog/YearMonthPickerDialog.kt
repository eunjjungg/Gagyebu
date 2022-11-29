package com.intern.gagyebu.dialog

import android.app.AlertDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.os.Bundle
import android.widget.NumberPicker
import androidx.fragment.app.DialogFragment
import com.intern.gagyebu.databinding.YearMonthPickerBinding
import com.intern.gagyebu.main.MainActivity.Companion.MONTH
import com.intern.gagyebu.main.MainActivity.Companion.YEAR
import java.util.*

class YearMonthPickerDialog : DialogFragment() {

    var customListener: OnDateSetListener? = null

    fun setListener(listener: OnDateSetListener?) {
        this.customListener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val binding = YearMonthPickerBinding.inflate(requireActivity().layoutInflater)

        val monthPicker = binding.pickerMonth
        val yearPicker = binding.pickerYear
        binding.cancel.setOnClickListener {
            this@YearMonthPickerDialog.dialog?.cancel()
        }
        binding.confirm.setOnClickListener {
            customListener!!.onDateSet(null, yearPicker.value, monthPicker.value, 0)
            this@YearMonthPickerDialog.dialog?.cancel()
        }
        monthPicker.minValue = 1
        monthPicker.maxValue = 12
        monthPicker.value = MONTH

        val year = YEAR
        yearPicker.minValue = MIN_YEAR
        yearPicker.maxValue = MAX_YEAR
        yearPicker.value = year
        builder.setView(binding.root)

        return builder.create()
    }

    companion object {
        private const val MAX_YEAR = 2099
        private const val MIN_YEAR = 1980
    }

}
