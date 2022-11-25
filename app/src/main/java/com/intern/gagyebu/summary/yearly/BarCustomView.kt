package com.intern.gagyebu.summary.yearly

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import com.intern.gagyebu.R

class BarCustomView: View {
    constructor(context: Context?): super(context)
    constructor(context: Context?, attrs: AttributeSet?): super(context, attrs) {
        initAttribute(attrs)
    }
    private var percentage: Float = 0f
    private var viewBound: RectF = RectF()
    private var drawBound: RectF = RectF()
    private var progress: Float = 0f

    private val paint = Paint().apply {
        flags = Paint.ANTI_ALIAS_FLAG
        style = Paint.Style.FILL
        color = resources.getColor(R.color.barChartBar)
    }
    private fun initAttribute(attributeSet: AttributeSet?) {
        if(attributeSet == null) {
            return
        }
        val attrs = context.theme.obtainStyledAttributes(
            attributeSet,
            R.styleable.BarCustomView,
            0,
            0
        )

        try {
            percentage = attrs.getFloat(
                R.styleable.BarCustomView_percentage,
                percentage
            )
        } finally {
            attrs.recycle()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        viewBound.let {
            it.left = paddingLeft.toFloat()
            it.top = paddingTop.toFloat()
            it.right = (measuredWidth - paddingRight).toFloat()
            it.bottom = (measuredHeight - paddingBottom).toFloat()
        }

        drawBound.let {
            it.left = paddingLeft.toFloat()
            it.top = paddingTop.toFloat()
            it.right = (measuredWidth - paddingRight).toFloat() * progress
            it.bottom = (measuredHeight - paddingBottom).toFloat()
        }



        canvas?.drawRoundRect(drawBound, 30f, 30f, paint)
    }

    private fun animationProcess() {
        val sec2 = 2000L
        val animationDuration = sec2 * percentage
        val animator = ValueAnimator.ofFloat(0f, percentage)
        animator.apply {
            duration = animationDuration.toLong()
            interpolator = DecelerateInterpolator()
            addUpdateListener { valueAnimator ->
                progress = valueAnimator.animatedValue as Float
                invalidate()
            }
        }
        animator.start()
    }

    fun setPercentage(inputPercentage: Float) {
        percentage = inputPercentage
        animationProcess()
    }
}