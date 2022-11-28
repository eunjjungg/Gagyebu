package com.intern.gagyebu.summary.yearly

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
class BoxCustomView: LinearLayout {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initAttribute(attrs, 0)
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initAttribute(attrs, defStyleAttr)
    }

    lateinit var themeTextView: TextView
    lateinit var averageTextView: TextView
    lateinit var costList1TextView: TextView
    lateinit var costList2TextView: TextView
    lateinit var costList3TextView: TextView


    var textList = mutableListOf<String>(
        "주 거 비", "평균보다 -18% 소비", "AUG : 1,123,432원", "SEP : 234,332원", "JAN : 4,332원"
    )
    private val TEXT_LIST_THEME = 0
    private val TEXT_LIST_AVERAGE = 1
    private val TEXT_LIST_COST1 = 2
    private val TEXT_LIST_COST2 = 3
    private val TEXT_LIST_COST3 = 4

    val minimumBoxCount = 3
    var boxCount = 1
        set(value) {
            field = value
            when(field) {
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
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20f, context.resources.displayMetrics)
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

        setTextView(reportYearBoxView, emphasisBoxDrawable, basicBoxDrawable)

        boxCount= typedArray.getInt(
            R.styleable.BoxCustomView_boxCount,
            minimumBoxCount
        )

        this.gravity = Gravity.CENTER
        typedArray.recycle()
    }

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
            .makeBaseTextView(basicBoxDrawable, 0 , 130).apply {
            }.also { addView(it) }
    }

    fun setTextAttribute(reportViewInfo: ReportViewInfo) {
        textList[TEXT_LIST_AVERAGE] = reportViewInfo.average
        textList[TEXT_LIST_COST2] = reportViewInfo.cost2 ?: ""
        textList[TEXT_LIST_THEME] = reportViewInfo.theme
        textList[TEXT_LIST_COST1] = reportViewInfo.cost1
        textList[TEXT_LIST_COST3] = reportViewInfo.cost3 ?: ""
        boxCount =
            if(reportViewInfo.cost2 == null) {
                3
            }
            else if(reportViewInfo.cost3 == null) {
                4
            }
            else {
                5
            }
        averageTextView.text = textList[TEXT_LIST_AVERAGE]
        costList2TextView.text = textList[TEXT_LIST_COST2]
        themeTextView.text = textList[TEXT_LIST_THEME]
        costList1TextView.text = textList[TEXT_LIST_COST1]
        costList3TextView.text = textList[TEXT_LIST_COST3]
    }




}