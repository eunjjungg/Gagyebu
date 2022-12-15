package com.intern.gagyebu.summary.monthly.compose

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.intern.gagyebu.summary.util.PieChartData

interface SliceDrawer {
    fun drawSlice(
        drawScope: DrawScope,
        canvas: Canvas,
        area: Size,
        startAngel: Float,
        drawAngle: Float,
        slice: PieChartData.Slice,
        _color: Color
    )
}