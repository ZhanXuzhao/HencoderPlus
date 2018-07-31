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
import android.view.ScaleGestureDetector
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
    private var maxScale = 0f
    private var minScale = 0f
    private var scaleThreshold = 0f // 双击时根据当前缩放比例判断是放大还是缩小的阈值
    private val scroller = OverScroller(context)
    var scaleFraction = 0f
        set(value) {
            field = value
            invalidate()
        }
    private lateinit var animator: ObjectAnimator
    private val gestureDetector: GestureDetector = GestureDetector(context, this)
    private val scaleGestureDetector = ScaleGestureDetector(context, object : ScaleGestureDetector.OnScaleGestureListener {
        private var beginFraction = 0f

        override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
            beginFraction = scaleFraction
            return true
        }

        override fun onScaleEnd(detector: ScaleGestureDetector?) {

        }

        override fun onScale(detector: ScaleGestureDetector?): Boolean {
            if (detector == null) {
                return false
            }
            scaleFraction = beginFraction * detector.scaleFactor
            scaleFraction = getValidValue(scaleFraction, minScale, maxScale)
            invalidate()
            return false
        }
    })

    init {
        gestureDetector.setOnDoubleTapListener(this)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) {
            return
        }
//        val scale = minScale + (maxScale - minScale) * scaleFraction
        val offsetFraction = (scaleFraction - minScale) / maxScale
        canvas.translate(offsetX * offsetFraction, offsetY * offsetFraction)
        canvas.scale(scaleFraction, scaleFraction, width / 2f, height / 2f)
        canvas.translate(originOffsetX, originOffsetY)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        originOffsetX = (measuredWidth / 2 - bitmapWidth / 2).toFloat()
        originOffsetY = (measuredHeight / 2 - bitmapHeight / 2).toFloat()
        val widthScale = 1f * measuredWidth / bitmapWidth
        val heightScale = 1f * measuredHeight / bitmapHeight
        minScale = Math.min(widthScale, heightScale)
        maxScale = Math.max(widthScale, heightScale) * SCALE_FRACTION
        scaleFraction = minScale
        scaleThreshold = Math.sqrt(minScale * maxScale.toDouble()).toFloat()
        animator = ObjectAnimator.ofFloat(this, "scaleFraction", minScale, maxScale)
        animator.addListener(this)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val onTouchEvent = gestureDetector.onTouchEvent(event)
        val onTouchEvent2 = scaleGestureDetector.onTouchEvent(event)
        if (!onTouchEvent && !onTouchEvent2) {
            return super.onTouchEvent(event)
        }
        return true
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
        offsetX = getValidValue(offsetX - distanceX, -getMaxOffsetX(), getMaxOffsetX())
        offsetY = getValidValue(offsetY - distanceY, -getMaxOffsetY(), getMaxOffsetY())
        invalidate()
        return false
    }

    override fun onLongPress(e: MotionEvent?) {}

    override fun onDoubleTap(e: MotionEvent?): Boolean {
        val scaleBig = scaleFraction < Math.sqrt(minScale * maxScale.toDouble())
        if (scaleBig) {
            offsetX = getOffsetX(e!!.x)
            offsetY = getOffsetY(e.y)
        }
        getScaleAnimator(scaleFraction, if (scaleBig) maxScale else minScale).start()
        return true
    }

    private fun getOffsetX(touchX: Float) =
            getValidValue(-(touchX - width / 2) * maxScale / 2, -getMaxOffsetX(), getMaxOffsetX())

    private fun getOffsetY(touchY: Float) =
            getValidValue(-(touchY - height / 2) * maxScale / 2, -getMaxOffsetY(), getMaxOffsetY())

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

    private fun getMaxOffsetY() = bitmapHeight * scaleFraction / 2f - height / 2f

    private fun getMaxOffsetX() = bitmapWidth * scaleFraction / 2f - width / 2f

    private fun getValidValue(target: Float, min: Float, max: Float): Float {
        return Math.max(Math.min(target, max), min)
    }

    private fun getScaleAnimator(startScale: Float, endScale: Float): ObjectAnimator {
        return ObjectAnimator.ofFloat(this, "scaleFraction", startScale, endScale)
    }
}
