package com.zxz.hencoderplus.lesson_12_multitouch

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
import android.graphics.PointF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * <pre>
 *     @author : Zhan Xuzhao
 *     time   : 2018/7/31 8:52
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class MultiTouchView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val pointList = ArrayList<PointF>()
    private var currentPointCount = 0;

    init {
        paint.strokeWidth = 100f
        paint.strokeCap = Paint.Cap.ROUND
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) {
            return true
        }
        currentPointCount = event.pointerCount
        while (pointList.size < currentPointCount) {
            pointList.add(PointF())
        }
        for (i: Int in 0..(currentPointCount - 1)) {
            pointList[i].x = event.getX(i)
            pointList[i].y = event.getY(i)
        }
        invalidate()
        return true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) {
            return
        }
        if (currentPointCount > 0) {
            for (i in 0..(currentPointCount - 1)) {
                canvas.drawPoint(pointList[i].x, pointList[i].y, paint)
            }
        }
    }
}