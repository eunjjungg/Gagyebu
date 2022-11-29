package com.intern.gagyebu.summary.yearly

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.intern.gagyebu.R
import com.intern.gagyebu.summary.util.ReportViewInfo
import kotlin.math.roundToInt

/**
 * width는 무조건 match_parent로 사용해야 함.
 */
class BoxCustomView : LinearLayout {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initAttribute(attrs, 0)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initAttribute(attrs, defStyleAttr)
    }

    lateinit var themeTextView: TextView
    lateinit var averageTextView: TextView
    lateinit var costList1TextView: TextView
    lateinit var costList2TextView: TextView
    lateinit var costList3TextView: TextView


    var textList = mutableListOf<String>(
        "", "", "", "", ""
    )
    private val TEXT_LIST_THEME = 0
    private val TEXT_LIST_AVERAGE = 1
    private val TEXT_LIST_COST1 = 2
    private val TEXT_LIST_COST2 = 3
    private val TEXT_LIST_COST3 = 4

    //최소 box들의 개수는 3개
    val minimumBoxCount = 3

    //box의 개수에 따라서 TEXT_LIST_COST2, 3의 가시 여부를 설정
    var boxCount = 1
        set(value) {
            field = value
            when (field) {
                3 -> {
                    costList2TextView.visibility = View.GONE
                    costList3TextView.visibility = View.GONE
                }
                4 -> {
                    costList2TextView.visibility = View.VISIBLE
                    costList3TextView.visibility = View.GONE
                }
                5 -> {
                    costList2TextView.visibility = View.VISIBLE
                    costList3TextView.visibility = View.VISIBLE
                }
            }
        }

    private var marginBtwBoxes: Float = 0f
    private var textSize: Float = 0f
    private var paddingHorizontal: Int = 0
    private var paddingVertical: Int = 0
    private var defaultBoxWidth: Int = 0

    //xml에서 값을 설정해주는 경우 사용되는 함수 (현재 코드에서는 사용할 필요 X)
    private fun initAttribute(attrs: AttributeSet?, defStyleAttr: Int) {
        orientation = VERTICAL

        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.BoxCustomView,
            defStyleAttr,
            0
        )

        //getDimension : dp -> px float return
        marginBtwBoxes = typedArray.getDimension(
            R.styleable.BoxCustomView_spacingBtwBoxes,
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                20f,
                context.resources.displayMetrics
            )
        )

        textSize = typedArray.getDimensionPixelSize(
            R.styleable.BoxCustomView_boxTextSize,
            resources.getDimensionPixelSize(R.dimen.boxTextSize)
        ).toFloat()

        paddingHorizontal = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 12f, context.resources.displayMetrics
        ).roundToInt()

        paddingVertical = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 12f, context.resources.displayMetrics
        ).roundToInt()

        defaultBoxWidth = resources.displayMetrics.widthPixels / 10 * 4

        //텍스트뷰를 만드는 클래스 객체(공통된 부분이 많아 클래스로 뺌)
        val reportYearBoxView = BoxTextCustomView(
            context,
            defaultBoxWidth,
            marginBtwBoxes,
            paddingHorizontal,
            paddingVertical,
            textSize
        )

        val basicBoxDrawable = resources.getDrawable(
            R.drawable.box_report_view_basic
        )
        val emphasisBoxDrawable = resources.getDrawable(
            R.drawable.box_report_view_emphasis
        )

        //위의 텍스트뷰를 만드는 클래스 객체로 실제 텍스트뷰를 만들어서 이 뷰에 붙여주는 함수
        setTextView(reportYearBoxView, emphasisBoxDrawable, basicBoxDrawable)

        //현재 따로 xml에서 설정해준 것이 없기 때문에 사용되지 않음.
        boxCount = typedArray.getInt(
            R.styleable.BoxCustomView_boxCount,
            minimumBoxCount
        )

        this.gravity = Gravity.CENTER

        typedArray.recycle()
    }

    //TextView를 직접 생성하고 뷰에 붙여주는 함수
    //지그재그 형식으로 만들어주기 위해서 margin 값을 각각 다르게 부여함
    private fun setTextView(
        reportYearBoxView: BoxTextCustomView,
        emphasisBoxDrawable: Drawable,
        basicBoxDrawable: Drawable
    ) {
        averageTextView = reportYearBoxView
            .makeBaseTextView(basicBoxDrawable, 130, 0).apply {
            }.also { addView(it) }

        costList2TextView = reportYearBoxView
            .makeBaseTextView(basicBoxDrawable, 0, 100).apply {
            }.also { addView(it) }

        themeTextView = reportYearBoxView
            .makeBaseTextView(emphasisBoxDrawable, 0, 0).apply {
            }.also { addView(it) }

        costList1TextView = reportYearBoxView
            .makeBaseTextView(basicBoxDrawable, 100, 0).apply {
            }.also { addView(it) }

        costList3TextView = reportYearBoxView
            .makeBaseTextView(basicBoxDrawable, 0, 130).apply {
            }.also { addView(it) }
    }

    //외부에서 커스텀뷰의 설정을 해주고 싶을 때 사용하는 함수
    //average, theme, cost1은 필수지만 cost2, cost3는 필수가 아니기에 널체크를 해줌
    fun setTextAttribute(reportViewInfo: ReportViewInfo) {
        boxCount =
            if (reportViewInfo.cost2 == null) {
                3
            } else if (reportViewInfo.cost3 == null) {
                4
            } else {
                5
            }

        averageTextView.text = reportViewInfo.average
        themeTextView.text = reportViewInfo.theme
        costList1TextView.text = reportViewInfo.cost1
        costList2TextView.text = reportViewInfo.cost2 ?: ""
        costList3TextView.text = reportViewInfo.cost3 ?: ""
    }


}