package com.intern.gagyebu.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.DismissValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.intern.gagyebu.R
import com.intern.gagyebu.room.ItemEntity
import java.text.DecimalFormat

@Composable
fun AccountRow(item: ItemEntity) {

    val date = stringResource(
        R.string.show_date_full,
        item.year,
        item.month,
        item.day
    )

    val color = when (item.category) {
        "수입" -> colorResource(id = R.color.income)

        else -> colorResource(id = R.color.spend)
    }

    val wonSign = if (item.category == "수입") "₩ " else "-₩ "
    val formattedAmount = formatAmount(item.amount)
    Column(modifier = Modifier
        .height(76.dp)
        .background(colorResource(id = R.color.colorPrimary))) {

        Spacer(modifier = Modifier.heightIn(10.dp))

        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val typography = MaterialTheme.typography
            AccountIndicator(
                color = color,
                modifier = Modifier
            )
            Spacer(Modifier.width(12.dp))
            Column(Modifier) {
                Text(text = date, style = typography.bodyLarge)
                CompositionLocalProvider() {
                    Text(text = item.title, style = typography.headlineMedium)
                }
            }
            Spacer(Modifier.weight(1f))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = wonSign,
                    style = typography.bodyLarge,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                Text(
                    text = formattedAmount,
                    style = typography.bodyLarge,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
            Spacer(Modifier.width(16.dp))

            val icon = when (item.category) {
                "수입" -> Icons.Outlined.Diamond
                "식료품" -> Icons.Outlined.Restaurant
                "주거비" -> Icons.Outlined.Home
                "교육비" -> Icons.Outlined.School
                "의료비" -> Icons.Outlined.LocalHospital
                "교통비" -> Icons.Outlined.DirectionsBus
                "통신비" -> Icons.Outlined.Phone
                "기타" -> Icons.Outlined.QuestionMark

                else -> Icons.Outlined.QuestionMark
            }

            CompositionLocalProvider() {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .size(24.dp)
                )
            }
        }
        Spacer(Modifier.heightIn(5.dp))

        ItemDivider()
    }
}

@Composable
private fun AccountIndicator(color: Color, modifier: Modifier = Modifier) {
    Spacer(
        modifier
            .size(4.dp, 36.dp)
            .background(color = color)
    )
}

@Composable
fun ItemDivider(modifier: Modifier = Modifier) {
    Divider(color = colorResource(id = R.color.gray), thickness = 1.dp, modifier = modifier)
}

fun formatAmount(amount: Int): String {
    return AmountDecimalFormat.format(amount)
}

private val AmountDecimalFormat = DecimalFormat("###,###")

