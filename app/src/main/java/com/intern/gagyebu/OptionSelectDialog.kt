package com.intern.gagyebu

import android.app.AlertDialog
import android.app.DatePickerDialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi

import androidx.fragment.app.DialogFragment
import com.intern.gagyebu.databinding.OptionSelectBinding
import kotlinx.coroutines.flow.callbackFlow


class OptionSelectDialog : DialogFragment() {

    var customListener: OptionDialogListener? = null

    fun setListener(listener: OptionDialogListener) {
        this.customListener = listener
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val binding = OptionSelectBinding.inflate(requireActivity().layoutInflater)

        var filter = "none"
        var order = "none"

        binding.filterGroup.setOnCheckedChangeListener { group, checkedId ->
            filter = when (checkedId) {
                binding.filterIncome.id -> "income"

                binding.filterSpend.id ->  "spend"

                else -> "all"
            }
        }

        binding.orderGroup.setOnCheckedChangeListener { group, checkedId ->
            order = when (checkedId) {
                binding.orderAmount.id -> "amount"

                else -> "day"
            }
        }

        binding.confirm.setOnClickListener{
            customListener?.option(filter, order)
            this.dialog?.cancel()
        }

        binding.cancel.setOnClickListener{
            this.dialog?.cancel()
        }

        builder.setView(binding.root)
        return builder.create()
    }

}