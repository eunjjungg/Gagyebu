package com.intern.gagyebu.summary.monthly.compose

import android.util.Log
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.intern.gagyebu.summary.util.PieChartData.Slice

class PieSliceDrawer(private val sliceLineWidth: Float = 25f) : SliceDrawer {
    private val piePainter = Paint().apply {
        isAntiAlias = true
        style = PaintingStyle.Stroke
    }

    override fun drawSlice(
        drawScope: DrawScope,
        canvas: Canvas,
        area: Size,
        startAngle: Float,
        drawAngle: Float,
        slice: Slice,
        _color: Color
    ) {
        val sliceLineWidth = calculateSliceLineWidthOffset(area = area)
        val drawableArea = calculateDrawableSize(area = area)
        canvas.drawArc(
            rect = drawableArea,
            paint = piePainter.apply {
                color = _color
                strokeWidth = sliceLineWidth
            },
            startAngle = startAngle,
            sweepAngle = drawAngle,
            useCenter = false
        )
    }

    private fun calculateSliceLineWidthOffset(area: Size): Float {
        val minSize = minOf(area.width, area.height)
        return minSize * (sliceLineWidth / 200f)
    }

    private fun calculateDrawableSize(area: Size): Rect {
        val sliceLineWidthOffset =
            calculateSliceLineWidthOffset(area = area) / 2f
        val horizontalOffset = (area.width - area.height) / 2f

        return Rect(
            left = sliceLineWidthOffset + horizontalOffset,
            top = sliceLineWidthOffset ,
            right = area.width - sliceLineWidthOffset - horizontalOffset,
            bottom = area.height - sliceLineWidthOffset
        )
    }
}