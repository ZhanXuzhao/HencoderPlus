package com.zxz.hencoderplus.lesson_07_clip_transform_animator_hardware

import android.content.Context
import android.graphics.Camera
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.blankj.utilcode.util.ConvertUtils
import com.zxz.hencoderplus.R
import com.zxz.hencoderplus.util.BitmapUtil

class Lesson07DemoView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {
    private val paint = Paint()
    private val size = ConvertUtils.dp2px(150f)
    private val bitmap = BitmapUtil.getBitmap(context, size, size, R.drawable.artanis)
    private val camera = Camera()


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) {
            return
        }
        val centerX = width / 2.0f
        val centerY = height / 2.0f
        val bWidth = bitmap.width.toFloat()
        val bHeight = bitmap.height.toFloat()
        val vWidth = width.toFloat()
        val vHeight = height.toFloat()
        val x = vWidth / 2 - bWidth / 2
        val y = vHeight / 2 - bHeight / 2
        val degree = 30f
//        canvas.clipRect(0.25f * bitmap.width, 0f, 0.75f * bitmap.width, bitmap.height.toFloat())
//        canvas.translate(300f, 0f)

//        canvas.rotate(degree, centerX, centerY)
//        canvas.clipRect(0f, 0f, centerX, vHeight)
//        canvas.rotate(-degree, centerX, centerY)

//        canvas.scale(1f, 2f, centerX, centerY)
//        canvas.drawBitmap(bitmap, centerX - bWidth / 2, centerY - bHeight / 2, paint)

//        canvas.skew(0.5f, 0.1f)
        canvas.translate(centerX, centerY)
        camera.rotateX(60f)
        camera.applyToCanvas(canvas)
        canvas.translate(-centerX, -centerY)
        canvas.drawBitmap(bitmap, centerX - bWidth / 2, centerY - bHeight / 2, paint)
    }
}