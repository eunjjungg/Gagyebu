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
import androidx.compose.ui.unit.dp
import java.text.DecimalFormat

@Composable
fun AccountRow(date: String, title: String, amount: Int, color: Color) {
    BaseRow(
        color = color,
        date = date,
        title = title,
        amount = amount,
        negative = false
    )
}

@Composable
private fun BaseRow(
    color: Color,
    date: String,
    title: String,
    amount: Int,
    negative: Boolean
) {
    val wonSign = if (negative) "–₩ " else "₩ "
    val formattedAmount = formatAmount(amount)
    Row(
        modifier = Modifier
            .height(68.dp),
                /*
            .clearAndSetSemantics {
                contentDescription =
                    "$date account ending in ${title.takeLast(4)}, current balance $dollarSign$formattedAmount"
            }
            ,
                 */
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

@Composable
private fun AccountIndicator(color: Color, modifier: Modifier = Modifier) {
    Spacer(modifier.size(4.dp, 36.dp).background(color = color))
}

@Composable
fun RallyDivider(modifier: Modifier = Modifier) {
    Divider(color = MaterialTheme.colorScheme.primary, thickness = 1.dp, modifier = modifier)
}


fun formatAmount(amount: Int): String {
    return AmountDecimalFormat.format(amount)
}

private val AmountDecimalFormat = DecimalFormat("###,###")

