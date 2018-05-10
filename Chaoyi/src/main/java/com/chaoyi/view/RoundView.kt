package com.chaoyi.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.util.AttributeSet
import android.view.View
import com.chaoyi.getDeviceWidth

/**
 * Created by yupenglei on 2018/4/27.
 */
class RoundView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var widthx: Int? = null
    private var point: Point? = null

    init {
        paint.color = Color.parseColor("#88FFFFFF")
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 10f
    }


    override fun onDraw(canvas: Canvas) {
        canvas.drawCircle(getDeviceWidth().div(2).toFloat(), 680f, 330f, paint)
    }
}