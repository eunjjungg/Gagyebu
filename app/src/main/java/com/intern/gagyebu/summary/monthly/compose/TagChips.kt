package com.intern.gagyebu.summary.monthly.compose

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.google.android.material.snackbar.Snackbar
import com.intern.gagyebu.R
import com.intern.gagyebu.summary.util.MonthlyDetailInfoWithState
import kotlinx.coroutines.launch


@Composable
fun CardChips(
    monthlyDetailInfoWithState: MonthlyDetailInfoWithState,
    chipClickInterface: ChipClickInterface,
    modifier: Modifier = Modifier
) {
    val imageVectorList = mutableListOf(
        Icons.Filled.Favorite,
        Icons.Filled.Article,
        Icons.Filled.CalendarMonth,
        Icons.Filled.Paid,
        Icons.Filled.Equalizer
    )

    LazyRow(
        modifier = modifier
    ) {
        itemsIndexed(monthlyDetailInfoWithState.getTagData()) { index, tagData ->
            MakeChips(
                text = tagData.content,
                category = tagData.sort,
                vector = imageVectorList[index],
                chipClickInterface
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MakeChips(
    text: String,
    category: String,
    vector: ImageVector,
    chipClickInterface: ChipClickInterface,
    modifier: Modifier = Modifier
) {
    val setLeadingIcon: @Composable (ImageVector) -> Unit = {
        Icon(
            imageVector = it,
            contentDescription = null,
            tint = colorResource(id = R.color.pieChartText)
        )
    }

    Column {
        AssistChip(
            onClick = {
                   chipClickInterface.onChipClick("$category : $text")
            } ,
            label = { Text(text) },
            modifier = modifier.padding(end = 8.dp),
            leadingIcon = { setLeadingIcon(vector) }
        )
    }

}

private fun MonthlyDetailInfoWithState.getTagData(): MutableList<TagData> = mutableListOf<TagData>(
    TagData("소비 제목", item.title),
    TagData("소비 카테고리", item.category),
    TagData("소비 날짜", "${item.year}.${item.month}.${item.day}"),
    TagData("소비 금액", "${item.amount}원"),
    TagData("이 카테고리의 소비 순위", "${order}위")
)


data class TagData(
    val sort: String,
    val content: String
)