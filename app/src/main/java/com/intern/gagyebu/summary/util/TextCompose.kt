package com.intern.gagyebu.summary.util

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.intern.gagyebu.R
import com.intern.gagyebu.summary.monthly.dpToSp


@Composable
fun TextTitle(
    text: String,
) {
    Text(
        text = text,
        modifier = Modifier.padding(vertical = 8.dp),
        style = MaterialTheme.typography.headlineMedium.copy(
            color = colorResource(id = R.color.pieChartText),
            fontSize = dpToSp(14.dp)
        )
    )
}

@Composable
fun TextContent(
    text: String,
) {
    Text(
        text = text,
        modifier = Modifier.padding(bottom = 12.dp),
        style = MaterialTheme.typography.bodyMedium.copy(
            color = colorResource(id = R.color.pieChartText),
            fontSize = dpToSp(11.dp)
        )
    )
}