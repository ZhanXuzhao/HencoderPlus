package com.zxz.hencoderplus.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory

/**
 * <pre>
 *     @author : Zhan Xuzhao
 *     time   : 2018/7/21 19:46
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class BitmapUtil {
    companion object {


        fun getBitmap(context: Context, targetWidth: Int, targetHeight: Int, drawableId: Int): Bitmap {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeResource(context.resources, drawableId, options)
            val originWidth = options.outWidth
            val originHeight = options.outHeight
            options.inJustDecodeBounds = false
            options.inSampleSize = Math.min(originWidth / targetWidth, originHeight / targetHeight)
            return BitmapFactory.decodeResource(context.resources, drawableId, options)
        }

        fun getSquareBitmap(context: Context, px: Int, drawableId: Int): Bitmap {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeResource(context.resources, drawableId, options)
            options.inJustDecodeBounds = false
            options.inDensity = options.outHeight
            options.inTargetDensity = px
            options.inJustDecodeBounds = false
            val targetSize = Math.min(Math.min(options.outWidth, options.outHeight), px)
            val bitmap = BitmapFactory.decodeResource(context.resources, drawableId, options)
            return Bitmap.createBitmap(bitmap, 0, 0, targetSize, targetSize)
        }

        fun getSquareBitmap(context: Context, drawableId: Int): Bitmap {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeResource(context.resources, drawableId, options)
            options.inJustDecodeBounds = false
            options.inDensity = options.outHeight
            val size = Math.min(options.outWidth, options.outHeight)
            options.inTargetDensity = size
            options.inJustDecodeBounds = false
            val targetSize = Math.min(options.outWidth, options.outHeight)
            val bitmap = BitmapFactory.decodeResource(context.resources, drawableId, options)
            return Bitmap.createBitmap(bitmap, 0, 0, targetSize, targetSize)
        }
    }
}