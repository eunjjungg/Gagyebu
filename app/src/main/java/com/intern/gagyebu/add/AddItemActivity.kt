package com.intern.gagyebu.add

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.intern.gagyebu.App
import com.intern.gagyebu.R
import com.intern.gagyebu.databinding.ActivityAddItemBinding
import com.intern.gagyebu.room.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

class AddItemActivity : AppCompatActivity(){

    private val viewModel: AddActivutyViewModel by viewModels()
    private val database = AppDatabase.getDatabase(App.context())
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        val binding =
            DataBindingUtil.setContentView<ActivityAddItemBinding>(this, R.layout.activity_add_item)
        binding.addViewModel = viewModel

        lifecycleScope.launch {
            viewModel.eventFlow.collect { event -> handleEvent(event) }
        }

        viewModel.date.observe(this){
            binding.date.text = it
        }

        viewModel.enableSave.observe(this){
            Log.d("clickable", it.toString())
            binding.save.isEnabled = it
        }

        binding.spinner.adapter = ArrayAdapter<Category>(
            this,
            android.R.layout.simple_spinner_item,
            enumValues<Category>()
        )
    }

    private fun handleEvent(event: AddActivutyViewModel.Event) = when (event) {
        is AddActivutyViewModel.Event.InputDate -> {
            val datePickerDialog = DatePickerDialog(
                this, { _, year, month, day ->
                    viewModel._date.value = "$year-${month+1}-$day"
                },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE)
            )
            datePickerDialog.show()
        }
        is AddActivutyViewModel.Event.Save ->{
            CoroutineScope(Dispatchers.IO).launch {
                database.itemDao().saveItem(event.value)
            }
            finish()
        }
        is AddActivutyViewModel.Event.Error ->{
            Toast.makeText(this, event.value, Toast.LENGTH_SHORT).show()
        }
    }
}