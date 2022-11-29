package com.intern.gagyebu.main


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.intern.gagyebu.R

@Composable
fun MonthlyDescription(MainViewModel: MainViewModel) {
    val incomeValue by MainViewModel.incomeValue.observeAsState()
    val spendValue by MainViewModel.spendValue.observeAsState()
    val totalValue by MainViewModel.totalValue.observeAsState()

    Column(){
        totalValue?.let {
            TotalView(it)
        }
        CompInfo(incomeValue, spendValue)

    }
}

@Composable
fun TotalView(it: Int) {
    val resource = stringResource(id = R.string.show_won)
    Text(
        text = resource.format(it),
        modifier = Modifier,
        style = LocalTextStyle.current.merge(
            TextStyle(
                fontSize = 30.sp
            )
        )
    )
}

@Composable
fun CompInfo(incomeValue: Int?, spendValue: Int?) {
    Box(modifier = Modifier.fillMaxWidth().height(100.dp),
    contentAlignment = Alignment.Center,
        propagateMinConstraints = false
    ) {
        ElevatedCard(modifier = Modifier.width(300.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .height(80.dp)
                    .width(300.dp)) {

                incomeValue?.let {
                    IncomeView(it)
                }

                Divider(
                    color = Color.Black,
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                )

                spendValue?.let {
                    SpendView(it)
                }
            }
        }
    }
}


@Composable
fun IncomeView(it: Int) {
    Column(modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "수입",
            modifier = Modifier,
            style = LocalTextStyle.current.merge(
                TextStyle(
                    fontSize = 20.sp
                )
            )
        )

        val resource = stringResource(id = R.string.show_won)
        Text(
            text = resource.format(it),
            modifier = Modifier,
        )
    }

}

@Composable
fun SpendView(it: Int) {
    Column(modifier = Modifier,
    horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "지출",
            modifier = Modifier,
            style = LocalTextStyle.current.merge(
                TextStyle(
                    fontSize = 20.sp
                )
            )
        )

        val resource = stringResource(id = R.string.show_won)
        Text(
            text = resource.format(it),
            modifier = Modifier
        )
    }
}

