package com.zxz.hencoderplus.lesson_07_clip_transform_animator_hardware

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.blankj.utilcode.util.ConvertUtils
import com.zxz.hencoderplus.R

/**
 * <pre>
 *     @author : Zhan Xuzhao
 *     time   : 2018/7/21 8:34
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class ThreeDimensionRotateView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paintOrange = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paintYellow = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paintLightGreen = Paint(Paint.ANTI_ALIAS_FLAG)

    private val camera = Camera()
    private val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.hencoder)
    private val size = ConvertUtils.dp2px(100f).toFloat()

    private val colorYellow = context.resources.getColor(R.color.translucent_yellow)


    var rightDegreeY = 0f
        set(value) {
            field = value
            invalidate()
        }
    var leftDegreeY = 0f
        set(value) {
            field = value
            invalidate()
        }
    var degreeZ = 0f
        set(value) {
            field = value
            invalidate()
        }

    init {

        paintOrange.color = context.resources.getColor(R.color.translucent_orange)
        paintYellow.color = context.resources.getColor(R.color.translucent_yellow)
        paintLightGreen.color = context.resources.getColor(R.color.translucent_light_green)
        camera.setLocation(0f, 0f, ConvertUtils.dp2px(6.0f).toFloat())
    }

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

        // draw right
        canvas.save()
        camera.save()

        canvas.translate(centerX, centerY)
        canvas.rotate(degreeZ)
        canvas.clipRect(-centerX, centerY, 0f, -centerY)    // 旋转之后 clip 得到一个 斜切的可绘制区域
        camera.rotateY((leftDegreeY))
        camera.applyToCanvas(canvas)
        canvas.rotate(-degreeZ)     // 反方向旋转回来，以便bitmap能够水品绘制
        canvas.translate(-centerX, -centerY)
        canvas.drawBitmap(bitmap, x, y, paint);

        canvas.restore()
        camera.restore()

        // draw right
        canvas.save()
        camera.save()

        canvas.translate(centerX, centerY)
        canvas.rotate(degreeZ)
        canvas.clipRect(0f, centerY, centerX, -centerY)
        camera.rotateY((rightDegreeY))
        camera.applyToCanvas(canvas)
        canvas.rotate(-degreeZ)
        canvas.translate(-centerX, -centerY)
        canvas.drawBitmap(bitmap, x, y, paint);

        canvas.restore()
        camera.restore()
    }

    private fun clipHalfRotateZ(canvas: Canvas, vWidth: Float, vHeight: Float, bWidth: Float, bHeight: Float) {
        canvas.save()
        camera.save()

        camera.rotateZ(30f)
        canvas.clipRect(vWidth / 2, 0f, vWidth, vHeight)
        canvas.translate(vWidth / 2, vHeight / 2)
        camera.applyToCanvas(canvas)
        canvas.translate(-vWidth / 2, -vHeight / 2)
        canvas.drawBitmap(bitmap, vWidth / 2 - bWidth / 2, vHeight / 2 - bHeight / 2, paintOrange);

        camera.restore()
        canvas.restore()
    }

    private fun clipHalfAndRotateY(canvas: Canvas, vWidth: Float, vHeight: Float, bWidth: Float, bHeight: Float) {

        // 平移canvas实现旋转
//        canvas.translate(centerX, centerY)
//        canvas.rotate(degreeZ)
//        canvas.clipRect(0f, -centerY, centerX, centerY)
//        canvas.drawRect(0f, 0f, vWidth, vHeight, paintYellow)
//        canvas.rotate(-degreeZ)
//        canvas.translate(-centerX, -centerY)
//        canvas.drawRect(0f, 0f, vWidth, vHeight, paintLightGreen)
//        canvas.drawBitmap(bitmap, x, y, paint);

        // 设置旋转中心 实现旋转
        camera.restore()
        canvas.restore()

        canvas.clipRect(vWidth / 2, 0f, vWidth, vHeight)
        camera.rotateY(45f)
        canvas.translate(vWidth / 2, vHeight / 2)
        camera.applyToCanvas(canvas)
        canvas.translate(-vWidth / 2, -vHeight / 2)
        canvas.drawBitmap(bitmap, vWidth / 2 - bWidth / 2, vHeight / 2 - bHeight / 2, paintOrange);

        camera.restore()
        canvas.restore()
    }

    private fun clipDemo(canvas: Canvas, centerX: Float, vHeight: Float, bHalfWidth: Float, centerY: Float, bHalfHeight: Float) {
        camera.restore()
        canvas.restore()

        canvas.clipRect(0f, 0f, centerX, vHeight)
        canvas.drawColor(colorYellow)
        canvas.drawBitmap(bitmap, centerX - bHalfWidth, centerY - bHalfHeight, paintOrange);

        camera.restore()
        canvas.restore()
    }

    private fun translateRectDemo(canvas: Canvas) {
        camera.restore()
        canvas.restore()

        canvas.translate(size.toFloat(), 0f)
        canvas.rotate(30f)
        canvas.drawColor(resources.getColor(R.color.translucent_gray))
        canvas.drawRect(-50f, 0f, width.toFloat(), height.toFloat(), paintLightGreen)
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paintYellow)
        canvas.drawRect(0f, 0f, size.toFloat(), size.toFloat(), paintOrange)

        camera.restore()
        canvas.restore()
    }


    private fun demoDraw(canvas: Canvas, centerX: Float, centerY: Float, bHalfWidth: Int, bHalfHeight: Int) {
        camera.restore()
        canvas.restore()

        canvas.save();
        camera.save(); // 保存 Camera 的状态
        camera.rotateX(30f); // 旋转 Camera 的三维空间
        canvas.translate(centerX, centerY); // 旋转之后把投影移动回来
        camera.applyToCanvas(canvas); // 把旋转投影到 Canvas
        canvas.translate(-centerX, -centerY); // 旋转之前把绘制内容移动到轴心（原点）
        camera.restore(); // 恢复 Camera 的状态
        canvas.drawBitmap(bitmap, centerX - bHalfWidth, centerY - bHalfHeight, paintOrange);

        camera.restore()
        canvas.restore()
    }

}