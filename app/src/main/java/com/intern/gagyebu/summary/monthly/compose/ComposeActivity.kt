package com.intern.gagyebu.summary.monthly.compose

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import com.intern.gagyebu.room.AppDatabase
import com.intern.gagyebu.room.ItemRepo
import com.intern.gagyebu.ui.theme.GagyebuTheme

class ComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GagyebuTheme {
                PieChart()
            }
        }
    }
}

@Composable
private fun PieChart() {
    val pieChartViewModel : PieChartViewModel = viewModel(factory = PieChartViewModel.PieChartViewModelFactory(
        ItemRepo.ItemRepository(AppDatabase.getDatabase(LocalContext.current).itemDao())
    ))
    val pieChartDataModel = remember { PieChartDataModel() }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .padding(
                horizontal = 0.dp,
                vertical = 24.dp
            )
            .verticalScroll(scrollState)

    ) {
        PieChartRow(pieChartDataModel)
    }
}

@Composable
private fun PieChartRow(pieChartDataModel: PieChartDataModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(vertical = 8.dp)
    ) {
        PieChart(
            pieChartData = pieChartDataModel.pieChartData,
            pieDrawer = PieSliceDrawer(
                sliceLineWidth = pieChartDataModel.sliceThickness
            )
        )
    }
}