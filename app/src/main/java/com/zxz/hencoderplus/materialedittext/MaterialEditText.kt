package com.zxz.hencoderplus.materialedittext

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.support.v7.widget.AppCompatEditText
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import com.blankj.utilcode.util.ConvertUtils

/**
 * <pre>
 *     @author : Zhan Xuzhao
 *     time   : 2018/7/22 8:46
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class MaterialEditText(context: Context, attributeSet: AttributeSet) : AppCompatEditText(context, attributeSet) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var accentColor = Color.BLACK
    private val animator = ObjectAnimator.ofFloat(this, "fraction", 0f, 1f)
    var fraction = 0f
        set(value) {
            field = value
            invalidate()
        }
    private var labelShown = false


    companion object {
        private val LABEL_TEXT_HEIGHT = ConvertUtils.dp2px(12f)
        private val LABEL_TEXT_TOP_PADDING = ConvertUtils.dp2px(4f)
        private val LABEL_TEXT_START_PADDING = ConvertUtils.dp2px(4f)
        private val LABEL_MOVING_SPACE = ConvertUtils.dp2px(12f)
    }

    init {
        val typedArray = context.obtainStyledAttributes(attributeSet, intArrayOf(android.R.attr.colorAccent))
        accentColor = typedArray.getColor(0, Color.BLACK)
        typedArray.recycle()

        setPadding(paddingLeft, paddingTop + LABEL_TEXT_HEIGHT + LABEL_TEXT_TOP_PADDING, paddingRight, paddingBottom)
        paint.textSize = ConvertUtils.dp2px(12f).toFloat()
        paint.color = accentColor

        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                showLabel(!TextUtils.isEmpty(s))
            }
        })
    }

    private fun showLabel(show: Boolean) {
        if (!labelShown && show) {
            labelShown = true
            animator.start()
        } else if (labelShown && !show) {
            labelShown = false
            animator.reverse()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) {
            return
        }
        val alpha = paint.alpha
        paint.alpha = (fraction * 0xff).toInt()
        val movingSpace = (1 - fraction) * LABEL_MOVING_SPACE
        if (hint != null) {
            canvas.drawText(hint, 0, hint.length, 0f + LABEL_TEXT_START_PADDING, 0f + LABEL_TEXT_HEIGHT + LABEL_TEXT_TOP_PADDING + movingSpace, paint)
        }
        paint.alpha = alpha
    }
}