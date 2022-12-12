package com.intern.gagyebu.main

import android.annotation.SuppressLint
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults.cardElevation
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.intern.gagyebu.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/** MainActivity ComposeMigration
 * 수입, 지출, 총합 View 컴포즈 이용하여 수정
 *
 */
@Composable
fun MonthlyDescription(MainViewModel: MainViewModel) {
    val incomeValue by MainViewModel.incomeValue.observeAsState()
    val spendValue by MainViewModel.spendValue.observeAsState()
    val totalValue by MainViewModel.totalValue.observeAsState()

    // sideEffect 상태 추적
    var showLoading by remember { mutableStateOf(true) }

    Column {
        if(showLoading){
            Landing(modifier = Modifier, onTimeout = {showLoading = false})
        }else{
            totalValue?.let {
                TotalView(it)
            }
        }
        CompInfo(incomeValue, spendValue)
    }
}

@Composable
fun Landing(modifier: Modifier, onTimeout: () -> Unit) {
    val animation = rememberInfiniteTransition()
    val alpha by animation.animateFloat(
        initialValue = 0f, targetValue = 1f, animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1000
                /**
                <500ms 0f ~ 0.7f to fast
                500~1000ms 0.7f ~ 1f to slow
                 */
                0.7f at 500
            },
            repeatMode = RepeatMode.Reverse
        )
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(Color.LightGray.copy(alpha = alpha))
    ){
        val timeout by rememberUpdatedState(onTimeout)
        /**
         * LaunchedEffect 내부 key가 변경되면 다시 시작 따라서 상수 값 등 사용하지 말고,
         * rememberUpdatedState 에 대해 공부하기
         */
        LaunchedEffect(true){
            delay(5000L)
            timeout()
        }
    }
}

@Composable
fun TotalView(it: String) {
    val resource = stringResource(id = R.string.show_won)
    Text(
        text = resource.format(it),
        modifier = Modifier,
        style = LocalTextStyle.current.merge(
            TextStyle(
                fontSize = 30.sp,
                color = colorResource(id = R.color.pieChartText)
            ),
        )
    )
}

@Composable
fun CompInfo(incomeValue: String?, spendValue: String?) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        contentAlignment = Alignment.Center,
        propagateMinConstraints = false
    ) {
        ElevatedCard(
            modifier = Modifier.width(300.dp),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(
                containerColor = colorResource(id = R.color.back),
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .height(80.dp)
                    .width(300.dp)
            ) {

                incomeValue?.let {
                    IncomeView(it)
                }

                Divider(
                    color = Color.Gray,
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
fun IncomeView(it: String) {
    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "수입",
            modifier = Modifier,
            style = LocalTextStyle.current.merge(
                TextStyle(
                    fontSize = 20.sp,
                    color = colorResource(id = R.color.pieChartText)
                )
            )
        )

        val resource = stringResource(id = R.string.show_won)
        Text(
            text = resource.format(it),
            modifier = Modifier,
            style = LocalTextStyle.current.merge(
                TextStyle(
                    fontSize = 15.sp,
                    color = colorResource(id = R.color.pieChartText)
                )
            )
        )
    }
}

@Composable
fun SpendView(it: String) {
    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "지출",
            modifier = Modifier,
            style = LocalTextStyle.current.merge(
                TextStyle(
                    fontSize = 20.sp,
                    color = colorResource(id = R.color.pieChartText)
                )
            )
        )

        val resource = stringResource(id = R.string.show_won)
        Text(
            text = resource.format(it),
            modifier = Modifier,
            style = LocalTextStyle.current.merge(
                TextStyle(
                    fontSize = 15.sp,
                    color = colorResource(id = R.color.pieChartText)
                )
            )
        )
    }
}