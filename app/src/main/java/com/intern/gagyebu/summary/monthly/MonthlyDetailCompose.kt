package com.intern.gagyebu.summary.monthly

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.intern.gagyebu.R
import com.intern.gagyebu.summary.util.MonthlyDetailInfo
import com.intern.gagyebu.ui.theme.cardBackgroundColor
import com.intern.gagyebu.ui.theme.cardTextColor

/**
 * 리팩토링 하지 않은 클래스
 */

@Composable
fun MonthlyDetailCompose(monthlyDetailViewModel: MonthlyDetailViewModel) {
    val topCostDetailList = monthlyDetailViewModel.topCostDetailList.observeAsState()

    topCostDetailList.value?.let {
        ComposeCards(
            topCostDetailList.value, modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 20.dp)
        )
    }
}

@Composable
fun CardContent(
    detail: MonthlyDetailInfo,
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
                .padding(8.dp)
        ) {
            TextTitle(text = detail.item.category)
            if (expanded) {
                TextSubTitle(text = stringResource(id = R.string.compose_openedDesc))
                TextContent(text = stringResource(id = R.string.compose_percentage, detail.percentage))
                TextContent(text = stringResource(id = R.string.compose_itemTitle, detail.item.title))
                TextContent(text = stringResource(id = R.string.compose_itemDate, detail.item.year, detail.item.month, detail.item.day))
            } else {
                TextSubTitle(text = stringResource(id = R.string.compose_closedDesc))
            }
        }
        IconButton(
            onClick = {
                expanded = !expanded
            }
        ) {
            Icon(
                imageVector = if (expanded) {
                    Icons.Filled.Close
                } else {
                    Icons.Filled.Add
                },
                tint = colorResource(cardTextColor),
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
fun TextTitle(
    text: String,
) {
    Text(
        text = text,
        modifier = Modifier.padding(vertical = 8.dp),
        style = MaterialTheme.typography.headlineMedium.copy(
            color = colorResource(id = R.color.pieChartText),
            fontSize = dpToSp(20.dp)
        )
    )
}

@Composable
fun TextSubTitle(
    text: String,
) {
    Text(
        text = text,
        modifier = Modifier.padding(vertical = 4.dp),
        style = MaterialTheme.typography.bodyLarge.copy(
            color = colorResource(id = R.color.pieChartText),
            fontSize = dpToSp(10.dp)
        )
    )
}

@Composable
fun TextContent(
    text: String,
) {
    Text(
        text = text,
        modifier = Modifier.padding(vertical = 4.dp),
        style = MaterialTheme.typography.bodyMedium.copy(
            color = colorResource(id = R.color.pieChartText),
            fontSize = dpToSp(12.dp)
        )
    )
}

@Composable
fun dpToSp(dp: Dp) = with(LocalDensity.current) { dp.toSp() }

@Composable
fun ComposeCard(
    detail: MonthlyDetailInfo
) {
    Card(
        modifier = Modifier
            .padding(vertical = 12.dp, horizontal = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = cardBackgroundColor)
        )
    ) {
        CardContent(detail)
    }
}

@Composable
fun ComposeCards(
    topCostDetailList: MutableList<MonthlyDetailInfo>?,
    modifier: Modifier = Modifier
) {
    if (topCostDetailList.isNullOrEmpty()) {
        return
    } else {
        LazyColumn(
            modifier = modifier
        ) {
            items(items = topCostDetailList) { item ->
                ComposeCard(
                    item
                )
            }
        }
    }
}
