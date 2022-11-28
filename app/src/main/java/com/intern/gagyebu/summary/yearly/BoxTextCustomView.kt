package com.intern.gagyebu.summary.yearly

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView

class BoxTextCustomView (
    val _context: Context,
    val _width: Int,
    val _marginBtwBoxes: Float,
    val _paddingHorizontal: Int,
    val _paddingVertical: Int,
    val _textSize: Float
) {
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
                setPadding(_paddingHorizontal, _paddingVertical, _paddingHorizontal, _paddingVertical)
                leftMargin = startMargin ?: 0
                rightMargin = endMargin ?: 0
            }
            background = boxColor
            //setTextSize(_textSize)
            setTextSize(TypedValue.COMPLEX_UNIT_DIP, _textSize)
            textAlignment = View.TEXT_ALIGNMENT_CENTER
        }
        return baseTextView
    }
}