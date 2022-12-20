package com.intern.gagyebu.summary.monthly

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.animation.addListener
import com.intern.gagyebu.R
import com.intern.gagyebu.summary.util.PieElement
import kotlin.math.roundToInt

/**
 * 리팩토링 하지 않은 클래스
 */

class PieChartView : View {
    constructor(context: Context?) : super(context) {
        initPaint()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
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

    //Paint 객체 각각 초기화
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

    //틀만 그려두고 drawArc의 각 시작점과 그리는 각도는
    //애니메이션을 진행하면서 정함
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

    //각 파이에 해당하는 애니메이터들을 정의해주고
    //첫 번째 파이를 그리는 valAnim0부터 start()하여 valAnim0 -> valAnim4까지 차례로 부르며 파이를 그림.
    private fun startAnimator() {
        val baseValueAnimator = BaseValueAnimator()

        val valAnim4 = baseValueAnimator.makeBaseValueAnimator(eachPieRangeList[4]).apply {
            addUpdateListener {
                eachPieProgess[4] = it.animatedValue as Float
                invalidate()
            }
        }
        val valAnim3 = baseValueAnimator.makeBaseValueAnimator(eachPieRangeList[3]).apply {
            addUpdateListener {
                eachPieProgess[3] = it.animatedValue as Float
                invalidate()
            }
            addListener(onEnd = {
                valAnim4.start()
            })
        }
        val valAnim2 = baseValueAnimator.makeBaseValueAnimator(eachPieRangeList[2]).apply {
            addUpdateListener {
                eachPieProgess[2] = it.animatedValue as Float
                invalidate()
            }
            addListener(onEnd = {
                valAnim3.start()
            })
        }
        val valAnim1 = baseValueAnimator.makeBaseValueAnimator(eachPieRangeList[1]).apply {
            addUpdateListener {
                eachPieProgess[1] = it.animatedValue as Float
                invalidate()
            }
            addListener(onEnd = {
                valAnim2.start()
            })
        }
        val valAnim0 = baseValueAnimator.makeBaseValueAnimator(eachPieRangeList[0]).apply {
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

    //중복되는 animator 생성 부분을 inner class로 뺌.
    inner class BaseValueAnimator {
        private val second1 = 1000L
        fun makeBaseValueAnimator(range: Float): ValueAnimator {
            return ValueAnimator.ofFloat(0f, range).apply {
                duration = (animationDuration * second1 * range / 360f).toLong()
                interpolator = LinearInterpolator()
                addUpdateListener { animator ->
                    animValue = animator.animatedValue as Float
                }
            }
        }
    }

    //뷰에서 이 커스텀 뷰를 설정하는 함수
    //뷰에서는 setPercentage를 통해 각 파이 원소들의 이름과 float을 정의해둔 elementList를 함께 전달
    fun setPercentage(elementList: List<PieElement>) {
        val _elementList = if (elementList.size > 5)
            elementList.subList(0, 5)
        else elementList

        for (i in _elementList.indices) {
            pieElements[i] = _elementList[i]
        }
        for (i in _elementList.size until pieElements.size) {
            pieElements[i] = PieElement("", 0f)
        }

        pieElements.sortByDescending { it!!.percentage }
        for (i in 0..pieElements.size - 1) {
            eachPieRangeList[i] = (360f * pieElements[i]!!.percentage).roundToInt().toFloat()
            if (i > 0) {
                eachPieStartAngle[i] = eachPieStartAngle[i - 1] + eachPieRangeList[i - 1]
            }
        }

        //소숫점 보정을 해주는 부분.
        //각 pie의 rangeList의 총합이 359, 361일 경우 마지막 pie의 값을 조절해서
        //총 pie의 rangeList의 총합이 360이 되도록 추가적으로 보정을 해주는 부분
        var check360 = 0f
        for (i in eachPieRangeList.indices) {
            check360 += eachPieRangeList[i]
        }
        if (check360 != 360f) {
            eachPieRangeList[_elementList.lastIndex] += 360f - check360
        }

        //보정 완료 후 애니메이터들을 정의해주는 startAnimator() 호출
        startAnimator()
    }
}