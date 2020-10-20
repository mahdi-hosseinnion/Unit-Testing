package com.example.unittesting.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText

public class LinedEditText
constructor(
    context: Context, attrs: AttributeSet
) : AppCompatEditText(
    context, attrs
) {
    private val mRect = Rect()
    private val mPaint = Paint()

    init {
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = 2F
        mPaint.color = 0xFFFFD966.toInt() // Color of the lines on paper
    }

    override fun onDraw(canvas: Canvas?) {
        //get the height of the view
        val height = (this.parent as View).height

        val lineHeight = lineHeight

        val numberOfLines = height / lineHeight

        var baseLine = getLineBounds(0, mRect)

        for (i in 0 until numberOfLines) {
            canvas?.drawLine(
                mRect.left.toFloat(),
                baseLine + 1f,
                mRect.right.toFloat(),
                baseLine + 1f,
                mPaint
            )

            baseLine += lineHeight
        }
        super.onDraw(canvas)
    }
}