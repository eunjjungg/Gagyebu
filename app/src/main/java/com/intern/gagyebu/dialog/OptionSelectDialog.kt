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
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first

/** 항목 정렬, 필터기능 제공을 위한 OptionSelectDialog **/

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

        //사용자가 기 저장한 필터, 정렬 옵션을 가져와 적용하도록 하는 lifecycleScope Block
        lifecycleScope.launch {
            val filterOption = dataStore.filterFlow.first()
            val orderOption = dataStore.orderFlow.first()

            Log.d("filter", filterOption)

            when (filterOption) {

                SelectableOptionsEnum.SPEND.toString() -> binding.filterSpend.isChecked = true

                SelectableOptionsEnum.INCOME.toString() -> binding.filterIncome.isChecked = true

                else -> binding.filterAll.isChecked = true
            }

            when (orderOption) {
                SelectableOptionsEnum.amount.toString() -> binding.orderAmount.isChecked = true

                else -> binding.orderDate.isChecked = true
            }

        }

        /**사용자가 확인 버튼을 클릭하는 경우
         * 1. optionListener 을 통한 값 전달.
         * 2. dialog 창 닫음
         */
        binding.confirm.setOnClickListener {
            val filter = if (binding.filterSpend.isChecked) {
                SelectableOptionsEnum.SPEND.toString()
            } else if (binding.filterIncome.isChecked) {
                SelectableOptionsEnum.INCOME.toString()
            } else {
                SelectableOptionsEnum.DEFAULT.toString()
            }

            val order = if (binding.orderAmount.isChecked) {
                SelectableOptionsEnum.amount.toString()
            } else {
                SelectableOptionsEnum.day.toString()
            }

            optionListener.option(filter, order)
            this.dialog?.cancel()
        }

        //사용자가 취소 버튼을 누르는 경우
        binding.cancel.setOnClickListener {
            this.dialog?.cancel()
        }

        builder.setView(binding.root)
        return builder.create()
    }

}