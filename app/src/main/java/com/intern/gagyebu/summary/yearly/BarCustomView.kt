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

class BarCustomView : View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initAttribute(attrs)
    }

    private var percentage: Float = 0f
    private var viewBound: RectF = RectF()
    private var drawBound: RectF = RectF()
    private var progress: Float = 0f
    var animator: ValueAnimator? = null

    private val paint = Paint().apply {
        flags = Paint.ANTI_ALIAS_FLAG
        style = Paint.Style.FILL
        color = resources.getColor(R.color.barChartBar)
    }

    private fun initAttribute(attributeSet: AttributeSet?) {
        if (attributeSet == null) {
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

    //progress 값을 valueAnimator로 그려야 되는 양만큼 움직이도록 설정
    //그래서 animator에서 invalidate() 해줘서 목표치만큼 그리도록 함
    //viewBound 내의 drawBound로 그릴 범위를 정해주고 이를 drawRoundRect로 그려줌
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

    //목표 percentage만큼 progress 값을 0f부터 점진적으로 증가시키는 역할
    //애니메이션 지속시간은 sec 변수로 설정
    private fun animationProcess() {
        val sec = 1000L
        val animationDuration = sec * percentage
        animator = ValueAnimator.ofFloat(0f, percentage)
        animator!!.apply {
            duration = animationDuration.toLong()
            interpolator = DecelerateInterpolator()
            addUpdateListener { valueAnimator ->
                progress = valueAnimator.animatedValue as Float
                invalidate()
            }
        }
        animator!!.start()
    }

    //이 커스텀뷰의 퍼센트를 정하는 함수
    //외부에서 커스텀뷰를 설정해줄 때 setPercentage(...)로 설정만 해주면 됨
    fun setPercentage(inputPercentage: Float) {
        percentage = inputPercentage
        animationProcess()
    }

    //현재 진행중인 애니메이션이 있다면 cancel해주는 함수
    fun destroyAnimator() {
        animator?.let {
            it.cancel()
        }
    }
}