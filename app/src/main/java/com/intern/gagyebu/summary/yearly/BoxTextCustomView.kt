package com.intern.gagyebu.summary.yearly

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView

//텍스트뷰를 생성하는데 필요한 보일러코드를 없애기 위한 클래스
class BoxTextCustomView(
    val _context: Context,
    val _width: Int,
    val _marginBtwBoxes: Float,
    val _paddingHorizontal: Int,
    val _paddingVertical: Int,
    val _textSize: Float
) {
    //실제 텍스트뷰를 생성해주는 함수
    //커스텀해야되는 값인 배경 색상, margin, 텍스트 내용을 제외하고는 모두 같게
    //여러 개의 텍스트뷰를 만들어주기 위해 생성함.
    fun makeBaseTextView(
        boxColor: Drawable,
        startMargin: Int?,
        endMargin: Int?
    ): TextView {
        val baseTextView = TextView(_context).apply {
            layoutParams = LinearLayout.LayoutParams(
                _width,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = _marginBtwBoxes.toInt()
                setPadding(
                    _paddingHorizontal,
                    _paddingVertical,
                    _paddingHorizontal,
                    _paddingVertical
                )
                leftMargin = startMargin ?: 0
                rightMargin = endMargin ?: 0
            }
            background = boxColor

            //그냥 setTextSize로 값을 넘겨주게 되면 dp가 아닌 sp로 적용이 됨.
            //setTextSize(_textSize)
            setTextSize(TypedValue.COMPLEX_UNIT_DIP, _textSize)
            textAlignment = View.TEXT_ALIGNMENT_CENTER
        }
        return baseTextView
    }
}