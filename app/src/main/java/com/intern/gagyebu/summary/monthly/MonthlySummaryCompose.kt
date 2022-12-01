package com.intern.gagyebu.summary.monthly

import android.preference.PreferenceActivity.Header
import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import com.intern.gagyebu.room.ItemEntity
import com.intern.gagyebu.R
import com.intern.gagyebu.summary.util.MonthlyDetailInfo
import com.intern.gagyebu.summary.util.PieElement
import com.intern.gagyebu.ui.theme.GagyebuTheme

@Composable
fun MonthlySummaryCompose(monthlyDetailViewModel: MonthlyDetailViewModel) {
    val topCostDetailList = monthlyDetailViewModel.topCostDetailList.observeAsState()

    topCostDetailList.value?.let {
        ComposeCards(topCostDetailList.value, modifier = Modifier.fillMaxSize())
    }

}

@Composable
fun CardContent(
    item: ItemEntity,
    percentage: Int
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    Row(
        modifier = Modifier
            .padding(12.dp)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(12.dp)
        ) {
            Text(text = item.category, modifier= Modifier, style = MaterialTheme.typography.bodyMedium)
            Text(text = stringResource(id = R.string.compose_closedDesc), modifier= Modifier)
            if(expanded) {
                Text(text = "expanded Text\nexpanded Text\nexpanded Text\nexpanded Text\nexpanded Text\nexpanded Text\n")
                Text(text = "expanded Text\nexpanded Text\nexpanded Text\nexpanded Text\nexpanded Text\nexpanded Text\n")
                Text(text = "expanded Text\nexpanded Text\nexpanded Text\nexpanded Text\nexpanded Text\nexpanded Text\n")
            }
        }
        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                imageVector = if (expanded) Icons.Filled.Close else Icons.Filled.Add,
                contentDescription = if (expanded) {
                    stringResource(R.string.compose_close)
                } else {
                    stringResource(R.string.compose_more)
                }
            )
        }
    }

}

@Composable
fun ComposeCard(
    item: ItemEntity,
    percentage: Int
) {
    Card(
        modifier = Modifier
            .padding(vertical = 30.dp, horizontal = 8.dp)
    ) {
        CardContent(item, percentage)
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewCardContent() {
    GagyebuTheme{
        ComposeCard(
            ItemEntity(1, 35000, "마카롱", 2022, 12, 1, "식료품"),
            80
        )
    }
}

@Composable
fun ComposeCards(
    topCostDetailList : MutableList<MonthlyDetailInfo>?,
    modifier: Modifier = Modifier
) {

    if(topCostDetailList.isNullOrEmpty()) {
        return
    } else {
        LazyColumn(
            modifier = modifier
        ) {
            item {
                ComposeCard(
                    item = ItemEntity(1, 35000, "마카롱", 2022, 12, 1, "식료품"),
                    percentage = 80
                )
            }
            items(items = topCostDetailList) { item ->
                Log.d("ccheck",item.toString())
                ComposeCard(
                    item = ItemEntity(1, 35000, "마카롱", 2022, 12, 1, "식료품"),
                    percentage = 80
                )
            }
        }
    }
}
