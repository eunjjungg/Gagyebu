package com.intern.gagyebu.dialog

import android.app.AlertDialog

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.intern.gagyebu.App
import com.intern.gagyebu.databinding.OptionDialogLayoutBinding
import com.intern.gagyebu.room.data.OptionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class OptionSelectDialog : DialogFragment() {

    private lateinit var optionListener: OptionDialogListener

    fun setListener(listener: OptionDialogListener) {
        this.optionListener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val binding = OptionDialogLayoutBinding.inflate(requireActivity().layoutInflater)
        val dataStore = OptionState(App.context())

        builder.setTitle("필터링")

        lifecycleScope.launch {
            val filterOption = dataStore.filterFlow.first()
            val orderOption = dataStore.orderFlow.first()

            when (filterOption) {
                Options.SPEND.toString() -> binding.filterSpend.isChecked = true

                Options.INCOME.toString() -> binding.filterIncome.isChecked = true

                else -> binding.filterAll.isChecked = true
            }

            when (orderOption) {
                Options.amount.toString() -> binding.orderAmount.isChecked = true

                else -> binding.orderDate.isChecked = true
            }

        }

        binding.confirm.setOnClickListener {
            val filter = if (binding.filterSpend.isChecked) {
                Options.SPEND.toString()
            } else if (binding.filterIncome.isChecked) {
                Options.INCOME.toString()
            } else {
                Options.DEFAULT.toString()
            }

            val order = if (binding.orderAmount.isChecked) {
                Options.amount.toString()
            } else {
                Options.day.toString()
            }

            CoroutineScope(Dispatchers.Main).launch {
                dataStore.setFilter(filter)
                dataStore.setOrder(order)
            }

            optionListener.option(filter, order)
            this.dialog?.cancel()
        }

        binding.cancel.setOnClickListener {
            this.dialog?.cancel()
        }

        builder.setView(binding.root)
        return builder.create()
    }

}