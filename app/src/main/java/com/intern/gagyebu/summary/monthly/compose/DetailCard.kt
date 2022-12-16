package com.intern.gagyebu.summary.monthly.compose

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.intern.gagyebu.R
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.intern.gagyebu.room.ItemEntity
import com.intern.gagyebu.summary.monthly.dpToSp
import com.intern.gagyebu.summary.util.MonthlyDetailInfoWithState
import com.intern.gagyebu.summary.util.TextContent
import com.intern.gagyebu.summary.util.TextTitle
import com.intern.gagyebu.ui.theme.cardBackgroundColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComposeCards(_viewModel: PieChartViewModel) {
    val viewModel = remember { _viewModel }

    Scaffold {
        Surface(
            modifier = Modifier
                .padding(it),
            color = Color.Transparent
        ) {
            LazyColumn() {
                items(items = viewModel.cardData) { item ->
                    MakeCard(monthlyDetailInfoWithState = item)
                }
            }
        }
    }

}

@Composable
fun MakeCard(
    monthlyDetailInfoWithState: MonthlyDetailInfoWithState,
    modifier: Modifier = Modifier
) {
    val shouldSnackbarOpen = remember { mutableStateOf<Boolean>(true) }
    val snackbarMessage = remember { mutableStateOf<String>("") }
    val snackBarState = remember { SnackbarHostState() }
    val chipClickInterface = object : ChipClickInterface {
        override fun onChipClick(msg: String) {
            shouldSnackbarOpen.value = !shouldSnackbarOpen.value
            snackbarMessage.value = msg
        }

    }

    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .padding( start = 24.dp, end = 24.dp, bottom = 48.dp)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        ),
        //TODO
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.pieChartBackground)
        )
    ) {
        Column {
            Image(
                painter = getPainterResource(category = monthlyDetailInfoWithState.item.category),
                contentDescription = null,
            )
            CardTextContent(
                monthlyDetailInfoWithState = monthlyDetailInfoWithState,
                chipClickInterface = chipClickInterface
            )
        }
        LaunchedEffect(key1 = shouldSnackbarOpen.value) {
            val result = snackBarState.showSnackbar(
                snackbarMessage.value,
                "확인",
                false,
                SnackbarDuration.Short
            ).let {
                when (it) {
                    SnackbarResult.Dismissed -> Log.d("TAG", "스낵바 닫아짐")
                    SnackbarResult.ActionPerformed -> Log.d("TAG", "MYSnackBar: 스낵바 확인 버튼 클릭")
                }
            }

        }
        if(snackbarMessage.value != "") {
            SnackbarHost(
                hostState = snackBarState,
                modifier = Modifier
            )
        }
    }
}

@Composable
private fun CardTextContent(
    monthlyDetailInfoWithState: MonthlyDetailInfoWithState,
    chipClickInterface: ChipClickInterface
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        CardTitle(monthlyDetailInfoWithState)
        CardChips(
            monthlyDetailInfoWithState = monthlyDetailInfoWithState,
            chipClickInterface = chipClickInterface
        )
        if(monthlyDetailInfoWithState.isOpen.value) {
            CardDetailContent(monthlyDetailInfoWithState)
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CardTitle(monthlyDetailInfoWithState: MonthlyDetailInfoWithState) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextTitle(text = monthlyDetailInfoWithState.item.category)
        FilterChip(
            onClick = {
                      monthlyDetailInfoWithState.isOpen.value = !monthlyDetailInfoWithState.isOpen.value
            } ,
            label = { Text("open") },
            modifier = Modifier
                .padding(end = 8.dp),
            selected = monthlyDetailInfoWithState.isOpen.value,
            trailingIcon = {
                Icon(
                    imageVector = monthlyDetailInfoWithState.isOpen.value.getLeadingIcon(),
                    contentDescription = null
                )
            },
            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Color(0x2A000000))
        )
    }
}

@Composable
private fun Boolean.getLeadingIcon(): ImageVector {
    return if(this) {
        Icons.Outlined.ExpandLess
    } else {
        Icons.Outlined.ExpandMore
    }
}

@Composable
private fun CardDetailContent(monthlyDetailInfo: MonthlyDetailInfoWithState) {
    Column(
        modifier = Modifier.padding(8.dp)
    ) {
        monthlyDetailInfo.apply {
            TextContent(
                text = stringResource(
                    id = R.string.compose_itemDetail,
                    order,
                    item.category,
                    item.category,
                    item.year,
                    item.month,
                    item.day,
                    item.title,
                    item.title,
                    item.amount
                )
            )
        }

    }
}

@Composable
private fun getPainterResource(category: String): Painter =
    when (category) {
        "식료품" -> painterResource(id = R.drawable.card_food)
        "주거비" -> painterResource(id = R.drawable.card_home)
        "교육비" -> painterResource(id = R.drawable.card_education)
        "의료비" -> painterResource(id = R.drawable.card_hospital)
        "교통비" -> painterResource(id = R.drawable.card_train)
        "통신비" -> painterResource(id = R.drawable.card_phone)
        else -> painterResource(id = R.drawable.card_etc)
    }

