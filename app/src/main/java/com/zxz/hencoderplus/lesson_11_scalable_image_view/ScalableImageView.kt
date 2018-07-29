package com.zxz.hencoderplus.lesson_11_scalable_image_view

import android.animation.Animator
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.OverScroller
import com.zxz.hencoderplus.R
import com.zxz.hencoderplus.util.BitmapUtil

/**
 * <pre>
 *     @author : Zhan Xuzhao
 *     time   : 2018/7/29 10:16
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class ScalableImageView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet), Runnable,
        GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener, Animator.AnimatorListener {

    companion object {
        private const val SCALE_FRACTION = 3f
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bitmap: Bitmap = BitmapUtil.getSquareBitmap(context, R.drawable.artanis)
    private val bitmapWidth = bitmap.width
    private val bitmapHeight = bitmap.height
    private var originOffsetX = 0f
    private var originOffsetY = 0f
    private var offsetX = 0f
    private var offsetY = 0f
    private var bigScale = 0f
    private var smallScale = 0f
    private val scroller = OverScroller(context)
    var fraction = 0f
        set(value) {
            field = value
            invalidate()
        }
    private var animator: ObjectAnimator = ObjectAnimator.ofFloat(this, "fraction", 0f, 1f)
    private var bigMode: Boolean = false
    private val gestureDetector: GestureDetector = GestureDetector(context, this)

    init {
        animator.addListener(this)
        gestureDetector.setOnDoubleTapListener(this)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) {
            return
        }
        val scale = smallScale + (bigScale - smallScale) * fraction
        canvas.translate(offsetX * fraction, offsetY * fraction)
        canvas.scale(scale, scale, width / 2f, height / 2f)
        canvas.translate(originOffsetX, originOffsetY)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        originOffsetX = (measuredWidth / 2 - bitmapWidth / 2).toFloat()
        originOffsetY = (measuredHeight / 2 - bitmapHeight / 2).toFloat()
        val widthScale = 1f * measuredWidth / bitmapWidth
        val heightScale = 1f * measuredHeight / bitmapHeight
        smallScale = Math.min(widthScale, heightScale)
        bigScale = Math.max(widthScale, heightScale) * SCALE_FRACTION
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val onTouchEvent = gestureDetector.onTouchEvent(event)
        if (!onTouchEvent) {
            super.onTouchEvent(event)
        }
        return onTouchEvent
    }

    override fun run() {
        if (scroller.computeScrollOffset()) {
            offsetX = scroller.currX.toFloat()
            offsetY = scroller.currY.toFloat()
            invalidate()
            postOnAnimation(this)
        }
    }

    override fun onShowPress(e: MotionEvent?) {}

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        return false
    }

    override fun onDown(e: MotionEvent?): Boolean {
        // 触摸时停止fling
        scroller.abortAnimation()
        return true
    }

    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
        scroller.fling(offsetX.toInt(), offsetY.toInt(),
                velocityX.toInt(), velocityY.toInt(),
                -getMaxOffsetX().toInt(), getMaxOffsetX().toInt(),
                -getMaxOffsetY().toInt(), getMaxOffsetY().toInt(),
                (getMaxOffsetX() * .1f).toInt(), (getMaxOffsetX() * .1f).toInt())
        postOnAnimation(this@ScalableImageView)
        return true
    }

    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
        if (bigMode) {
            offsetX = getValidValue(offsetX - distanceX, -getMaxOffsetX(), getMaxOffsetX())
            offsetY = getValidValue(offsetY - distanceY, -getMaxOffsetY(), getMaxOffsetY())
            invalidate()
        }
        return false
    }

    override fun onLongPress(e: MotionEvent?) {}

    override fun onDoubleTap(e: MotionEvent?): Boolean {
        bigMode = !bigMode
        if (bigMode) {
            animator.start()
            offsetX = getValidValue(-(e!!.x - width / 2) * bigScale / 2, -getMaxOffsetX(), getMaxOffsetX())
            offsetY = getValidValue(-(e.y - height / 2) * bigScale / 2, -getMaxOffsetY(), getMaxOffsetY())
        } else {
            animator.reverse()
        }
        return true
    }

    override fun onDoubleTapEvent(e: MotionEvent?): Boolean {
        return false
    }

    override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
        return false
    }

    override fun onAnimationRepeat(animation: Animator?) {}

    override fun onAnimationEnd(animation: Animator?) {}

    override fun onAnimationEnd(animation: Animator?, isReverse: Boolean) {
        if (isReverse) {
            offsetX = 0f
            offsetY = 0f
        }
    }

    override fun onAnimationCancel(animation: Animator?) {}

    override fun onAnimationStart(animation: Animator?) {}

    private fun getMaxOffsetY() = bitmapHeight * bigScale / 2f - height / 2f

    private fun getMaxOffsetX() = bitmapWidth * bigScale / 2f - width / 2f

    private fun getValidValue(target: Float, min: Float, max: Float): Float {
        return Math.max(Math.min(target, max), min)
    }
}