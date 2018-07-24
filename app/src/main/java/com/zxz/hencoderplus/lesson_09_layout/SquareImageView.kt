package com.zxz.hencoderplus.lesson_09_layout

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView

/**
 * <pre>
 *     @author : Zhan Xuzhao
 *     time   : 2018/7/24 8:01
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class SquareImageView(context: Context, attributeSet: AttributeSet) : ImageView(context, attributeSet) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = measuredWidth
        val height = measuredHeight
        val size = Math.min(width, height)
        setMeasuredDimension(size, size)
    }
}