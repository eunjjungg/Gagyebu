package com.intern.gagyebu.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.intern.gagyebu.add.AddItemActivity
import com.intern.gagyebu.App
import com.intern.gagyebu.databinding.ActivityMainBinding
import com.intern.gagyebu.room.AppDatabase
import com.intern.gagyebu.room.ItemRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val database = AppDatabase.getDatabase(App.context())
    private val factory = MainViewModelFactory(ItemRepo(database.itemDao()))

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        viewModel.resetOrder()

        binding.recyclerview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val adapter = Adapter()
        binding.recyclerview.adapter = adapter
        subscribeUi(adapter, viewModel)

        binding.add.setOnClickListener{
            startActivity(Intent(this, AddItemActivity::class.java))
        }

        binding.delete.setOnClickListener{
            CoroutineScope(Dispatchers.IO).launch {
                database.itemDao()
                    .deleteItem(binding.optionNumber.text.toString().toInt())
            }
        }

        binding.order.setOnClickListener{
            viewModel.setOrder(binding.optionNumber.text.toString().toInt())
        }
    }

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