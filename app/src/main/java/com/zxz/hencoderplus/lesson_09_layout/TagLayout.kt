package com.zxz.hencoderplus.lesson_09_layout

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import java.util.ArrayList

/**
 * <pre>
 *     @author : Zhan Xuzhao
 *     time   : 2018/7/24 8:31
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class TagLayout(context: Context, attributeSet: AttributeSet) : ViewGroup(context, attributeSet) {
    private var rects: ArrayList<Rect> = ArrayList()

    override fun onViewAdded(child: View?) {
        super.onViewAdded(child)
        if(rects.size < childCount) {
            rects.add(Rect())
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var widthUsed = 0
        var heightUsed = 0
        var currentLineTop = 0
        var currentLineMaxHeight = 0
        var maxWidthUsed = 0
        for (i in 0..(childCount - 1)) {
            val child = getChildAt(i)
            if (child.measuredWidth + widthUsed > MeasureSpec.getSize(widthMeasureSpec)) {
                widthUsed = 0
                currentLineTop += currentLineMaxHeight
                currentLineMaxHeight = child.measuredHeight
                heightUsed = currentLineTop + child.measuredHeight
            }
            measureChildWithMargins(child, widthMeasureSpec, widthUsed, heightMeasureSpec, heightUsed)
            val rect = rects[i]
            rect.left = widthUsed
            rect.top = currentLineTop
            rect.right = rect.left + child.measuredWidth
            rect.bottom = rect.top + child.measuredHeight
            rects.add(rect)

            currentLineMaxHeight = Math.max(currentLineMaxHeight, child.measuredHeight)
            widthUsed += child.measuredWidth
            heightUsed = currentLineTop + currentLineMaxHeight
            maxWidthUsed = Math.max(maxWidthUsed, widthUsed)
        }
        setMeasuredDimension(maxWidthUsed, heightUsed)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        for (i in 0..(childCount - 1)) {
            val child = getChildAt(i)
            child.layout(rects[i].left, rects[i].top, rects[i].right, rects[i].bottom)
        }
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }
}