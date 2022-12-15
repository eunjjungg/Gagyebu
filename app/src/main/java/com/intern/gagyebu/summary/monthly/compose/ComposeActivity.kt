package com.intern.gagyebu.summary.monthly.compose

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.material.snackbar.Snackbar
import com.intern.gagyebu.room.AppDatabase
import com.intern.gagyebu.room.ItemRepo
import com.intern.gagyebu.summary.monthly.PieChartView
import com.intern.gagyebu.summary.util.DateInfo
import com.intern.gagyebu.ui.theme.GagyebuTheme

class ComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel by viewModels<PieChartViewModel> {
            PieChartViewModel.PieChartViewModelFactory(
                ItemRepo.ItemRepository(
                    itemDao = AppDatabase.getDatabase(this@ComposeActivity).itemDao()
                )
            )
        }
        intent.getParcel().also {
            viewModel.apply {
                getTopCostList(it)
                getCardList(it)
            }
        }

        setContent {
            GagyebuTheme {
                TopActivity(viewModel)
            }
        }
    }
}

@Composable
private fun TopActivity(viewModel: PieChartViewModel) {
    Column(modifier = Modifier.background(MaterialTheme.colorScheme.surface)) {
        PieChartTopLevel(viewModel)
        ComposeCards(viewModel)
    }
}

private fun Intent.getParcel(): DateInfo {
    val bundle = this.extras

    val dateInfo: DateInfo? = bundle?.getParcelable("dateInfo")

    dateInfo?.let {
        return it
    }
    return DateInfo(0 , 0)
}
