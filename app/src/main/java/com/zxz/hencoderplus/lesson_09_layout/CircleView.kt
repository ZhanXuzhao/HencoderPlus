package com.zxz.hencoderplus.lesson_09_layout

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.blankj.utilcode.util.ConvertUtils
import com.zxz.hencoderplus.R

/**
 * <pre>
 *     @author : Zhan Xuzhao
 *     time   : 2018/7/24 8:13
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class CircleView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {
    private val paint = Paint()

    companion object {
        private val PADDING = ConvertUtils.dp2px(20f)
        private val RADIUS = ConvertUtils.dp2px(50f)
    }

    init {
        paint.color = context.getColor(R.color.color_yellow)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val size = (PADDING + RADIUS) * 2
        setMeasuredDimension(resolveSizeAndState(size, widthMeasureSpec, 0),
                resolveSizeAndState(size, heightMeasureSpec, 0))
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) {
            return
        }
        canvas.drawColor(context.getColor(R.color.color_blue))
        canvas.drawCircle(width / 2f, height / 2f, RADIUS.toFloat(), paint)
    }









}