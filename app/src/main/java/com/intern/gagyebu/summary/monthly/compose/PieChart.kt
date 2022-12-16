package com.intern.gagyebu.summary.monthly.compose

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.TweenSpec
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.intern.gagyebu.R
import com.intern.gagyebu.summary.util.PieChartData
import com.intern.gagyebu.summary.util.PieChartUtils

@Composable
fun PieChartTopLevel(viewModel: PieChartViewModel) {
    val pieChartViewModel = remember { viewModel }

    Column(
        modifier = Modifier
            .padding(
                horizontal = 0.dp,
                vertical = 48.dp
            )
    ) {
        PieChartRow(pieChartViewModel)
    }
}

@Composable
private fun PieChartRow(pieChartViewModel: PieChartViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(vertical = 8.dp)
    ) {
        PieChart(
            pieChartData = pieChartViewModel.pieChartData,
            pieDrawer = PieSliceDrawer(
                sliceLineWidth = pieChartViewModel.sliceThickness
            )
        )
    }
}

@Composable
fun PieChart(
    pieChartData: PieChartData,
    modifier: Modifier = Modifier,
    // <T : Any?> TweenSpec(durationMillis: Int, delay: Int, easing: Easing)
    animation: AnimationSpec<Float> = TweenSpec<Float>(durationMillis = 500),
    pieDrawer: SliceDrawer = PieSliceDrawer()
) {
    // remember 중 key (여기서는 pieChartData.slices) 값이 변경했을 때 { } calculation을 다시 수행함.
    // slices에 대한 값이 바뀌었을 때 Animatable(initialValue = 0f) 연산을 다시 수행함.
    val transitionProgress = remember(pieChartData.slices) { Animatable(initialValue = 0f) }

    // LaunchedEffect는 한 번만 실행되어야 하는 작업이 있을 때 사용
    // key 값인 pieChartData.slices가 바뀌었을 때만 { ... }의 이전 작업이 있을 경우 취소하고 다시 실행함.
    // 무조건 한 번만 실행되어야 하는 경우에는 LaunchedEffect(true) { ... } 방법으로로사용
    // 결론적으로 pieChartData.slices가 바뀌면 transitionProgress의 값이 바뀌고 그에 따라 애니메이션도 다시 처음부터 실행되는 구조
    LaunchedEffect(pieChartData.slices) {
        transitionProgress.animateTo(targetValue = 1f, animationSpec = animation)

    }

    // progress 값이 바뀔 때마다 DrawChart 함수가 불리게 됨
    // 왜냐하면 remember의 Animatable 값이 바뀔때마다 리컴포지션 되기 때문임.
    DrawChart(
        pieChartData = pieChartData,
        modifier = modifier.fillMaxSize(),
        progress = transitionProgress.value,
        pieDrawer = pieDrawer
    )
}

@Composable
private fun DrawChart(
    pieChartData: PieChartData,
    modifier: Modifier,
    progress: Float,
    pieDrawer: SliceDrawer
) {
    val slices = pieChartData.slices
    val colors = listOf<Color>(
        colorResource(id = R.color.pieChart0),
        colorResource(id = R.color.pieChart1),
        colorResource(id = R.color.pieChart2),
        colorResource(id = R.color.pieChart3),
        colorResource(id = R.color.pieChart4)
    )
    // import androidx.compose.foundation.Canvas (ui.graphics의 canvas가 아님)
    /**
     * 파이 차트에 그려질 모든 원소에 대해 모두 시작점을 0으로 시작해서
     * 그려져야 되는 시간 동안 자신의 퍼센테이지만큼 그리도록 함.
     * 따라서 값을 보정해줘야 할 필요가 없음.
     */
    Canvas(modifier = modifier) {
        drawIntoCanvas {
            var startArc = 0f

            slices.indices.forEach { index ->
                val drawArc = PieChartUtils.calculateAngle(
                    sliceLength = slices[index].percentage,
                    totalLength = pieChartData.totalSize,
                    progress = progress
                )

                pieDrawer.drawSlice(
                    drawScope = this,
                    canvas = drawContext.canvas,
                    area = drawContext.size,
                    startAngel = startArc,
                    drawAngle = drawArc,
                    slice = slices[index],
                    _color = colors[index]
                )

                startArc += drawArc
            }
        }
    }
}

// not working
@Preview
@Composable
fun PieChartPreview() = PieChart(pieChartData = PieChartData(
    slices = listOf(
        PieChartData.Slice(25f, "dd"),
        PieChartData.Slice(42f, "Ddd"),
        PieChartData.Slice(23f, "eee")
    )
))