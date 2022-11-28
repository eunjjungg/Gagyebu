package com.intern.gagyebu.summary.monthly

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.animation.addListener
import com.intern.gagyebu.R
import com.intern.gagyebu.summary.util.PieElement
import kotlin.math.roundToInt

class PieChartView: View {
    constructor(context: Context?) : super(context) {
        initPaint()
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initAttribute(attrs)
        initPaint()
    }

    private val paintList: MutableList<Paint> = mutableListOf()
    private val pieElements: MutableList<PieElement?> =
        mutableListOf(null, null, null, null, null)
    private val eachPieRangeList: MutableList<Float> = mutableListOf(0f, 0f, 0f, 0f, 0f)
    private val viewBound: RectF = RectF()
    private var animValue: Float = 0f
    private var animationDuration: Long = 1L
    private val eachPieStartAngle = mutableListOf<Float>(-90f, 0f, 0f, 0f, 0f)
    private val eachPieProgess = mutableListOf<Float>(0f, 0f, 0f, 0f, 0f)


    private fun initAttribute(attributeSet: AttributeSet?) {
        if (attributeSet == null) {
            return
        }

        val attrs = context.theme.obtainStyledAttributes(
            attributeSet,
            R.styleable.PieChartView,
            0,
            0
        )

        try {


        } finally {
            attrs.recycle()
        }


    }

    private fun initPaint() {
        val setPaintingLambda: (Int) -> Paint = {
            Paint().apply {
                flags = Paint.ANTI_ALIAS_FLAG
                style = Paint.Style.FILL_AND_STROKE
                color = resources.getColor(it)
            }
        }
        paintList.apply {
            addAll(
                listOf(
                    setPaintingLambda(R.color.pieChart0),
                    setPaintingLambda(R.color.pieChart1),
                    setPaintingLambda(R.color.pieChart2),
                    setPaintingLambda(R.color.pieChart3),
                    setPaintingLambda(R.color.pieChart4),
                )
            )
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

        canvas?.drawArc(
            viewBound,
            eachPieStartAngle[0],
            eachPieProgess[0],
            true,
            paintList[0]
        )
        canvas?.drawArc(
            viewBound,
            eachPieStartAngle[1],
            eachPieProgess[1],
            true,
            paintList[1]
        )
        canvas?.drawArc(
            viewBound,
            eachPieStartAngle[2],
            eachPieProgess[2],
            true,
            paintList[2]
        )
        canvas?.drawArc(
            viewBound,
            eachPieStartAngle[3],
            eachPieProgess[3],
            true,
            paintList[3]
        )
        canvas?.drawArc(
            viewBound,
            eachPieStartAngle[4],
            eachPieProgess[4],
            true,
            paintList[4]
        )
    }

    fun startAnimator() {
        val baseValueAnimator = BaseValueAnimator()

        val valAnim4 = baseValueAnimator.makeBaseValueAnimaotor(eachPieRangeList[4]).apply {
            addUpdateListener {
                eachPieProgess[4] = it.animatedValue as Float
                invalidate()
            }
        }
        val valAnim3 = baseValueAnimator.makeBaseValueAnimaotor(eachPieRangeList[3]).apply {
            addUpdateListener {
                eachPieProgess[3] = it.animatedValue as Float
                invalidate()
            }
            addListener(onEnd = {
                valAnim4.start()
            })
        }
        val valAnim2 = baseValueAnimator.makeBaseValueAnimaotor(eachPieRangeList[2]).apply {
            addUpdateListener {
                eachPieProgess[2] = it.animatedValue as Float
                invalidate()
            }
            addListener(onEnd = {
                valAnim3.start()
            })
        }
        val valAnim1 = baseValueAnimator.makeBaseValueAnimaotor(eachPieRangeList[1]).apply {
            addUpdateListener {
                eachPieProgess[1] = it.animatedValue as Float
                invalidate()
            }
            addListener(onEnd = {
                valAnim2.start()
            })
        }
        val valAnim0 = baseValueAnimator.makeBaseValueAnimaotor(eachPieRangeList[0]).apply {
            addUpdateListener {
                eachPieProgess[0] = it.animatedValue as Float
                invalidate()
            }
            addListener(onEnd = {
                valAnim1.start()
            })
        }

        valAnim0.start()



    }

    inner class BaseValueAnimator {
        private val second1 = 1000L
        fun makeBaseValueAnimaotor(range: Float): ValueAnimator {
            return ValueAnimator.ofFloat(0f, range).apply {
                duration = (animationDuration * second1 * range / 360f).toLong()
                interpolator = LinearInterpolator()
                addUpdateListener { animator ->
                    animValue = animator.animatedValue as Float
                }
            }
        }
    }


    fun setPercentage(elementList: List<PieElement>){
        val _elementList = if(elementList.size > 5)
            elementList.subList(0, 5)
        else elementList

        for(i in _elementList.indices) {
            pieElements[i] = _elementList[i]
        }
        for(i in _elementList.size until pieElements.size) {
            pieElements[i] = PieElement("", 0f)
        }
        pieElements.sortByDescending { it!!.percentage }
        Log.d("pieElements", pieElements.toString())
        for(i in 0..pieElements.size - 1) {
            eachPieRangeList[i] = (360f * pieElements[i]!!.percentage).roundToInt().toFloat()
            if(i > 0) {
                eachPieStartAngle[i] = eachPieStartAngle[i - 1] + eachPieRangeList[i - 1]
            }
        }
        startAnimator()
    }
}