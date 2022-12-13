package com.intern.gagyebu.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.intern.gagyebu.R
import java.text.DecimalFormat

@Composable
fun AccountRow(
    color: Color,
    date: String,
    title: String,
    amount: Int
) {
    val wonSign = if (true) "–₩ " else "₩ "
    val formattedAmount = formatAmount(amount)
    Column {
        Row(
            modifier = Modifier
                .height(68.dp)
                .background(color = colorResource(id = R.color.barChartBar)),

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
                    Text(text = title, style = typography.headlineMedium)
                }
            }
            Spacer(Modifier.weight(1f))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = wonSign,
                    style = typography.bodyMedium,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                Text(
                    text = formattedAmount,
                    style = typography.bodyMedium,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
            Spacer(Modifier.width(16.dp))

            CompositionLocalProvider() {
                Icon(
                    imageVector = Icons.Filled.ChevronRight,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .size(24.dp)
                )
            }
        }
        RallyDivider()
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
fun RallyDivider(modifier: Modifier = Modifier) {
    Divider(color = MaterialTheme.colorScheme.primary, thickness = 1.dp, modifier = modifier)
}


fun formatAmount(amount: Int): String {
    return AmountDecimalFormat.format(amount)
}

private val AmountDecimalFormat = DecimalFormat("###,###")

