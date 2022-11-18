package com.intern.gagyebu.summary.yearly

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.viewModelScope
import com.intern.gagyebu.App
import com.intern.gagyebu.R
import com.intern.gagyebu.databinding.ActivityRoomTestBinding
import com.intern.gagyebu.room.AppDatabase
import com.intern.gagyebu.room.ItemDao
import com.intern.gagyebu.room.ItemEntity
import com.intern.gagyebu.room.ItemRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class RoomTestActivity : AppCompatActivity() {
    private val binding by lazy {
        DataBindingUtil
            .setContentView<ActivityRoomTestBinding>(
                this@RoomTestActivity, R.layout.activity_room_test
            )
    }
    private val itemDao by lazy {
        AppDatabase.getDatabase(applicationContext).itemDao()
    }
    private val viewModel: RoomTestViewModel by lazy {
        ViewModelProvider(
            this, RoomTestViewModel.RoomTestViewModelFactory(
                ItemRepository(itemDao)
            )
        ).get(RoomTestViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            viewmodel = viewModel
            lifecycleOwner = this@RoomTestActivity
        }

    }


}