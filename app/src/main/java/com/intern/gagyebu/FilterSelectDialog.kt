package com.intern.gagyebu

import android.app.AlertDialog

import android.app.Dialog
import android.os.Bundle

import androidx.fragment.app.DialogFragment
import com.intern.gagyebu.databinding.FilterSelectBinding


class FilterSelectDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val binding = FilterSelectBinding.inflate(requireActivity().layoutInflater)

        builder.setView(binding.root)

        return builder.create()
    }

}