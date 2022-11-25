package com.intern.gagyebu.dialog

import android.app.AlertDialog

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.intern.gagyebu.databinding.OptionSelectBinding

class OptionSelectDialog : DialogFragment() {

    var customListener: OptionDialogListener? = null

    fun setListener(listener: OptionDialogListener) {
        this.customListener = listener
    }

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