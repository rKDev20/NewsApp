package com.sample.newsapp.glide.transformations

import android.graphics.*
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest

class GradientTransformation : BitmapTransformation() {
    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.digest("GlideTransformation".encodeToByteArray())
    }

    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        val linearGradient = LinearGradient(
            0f,
            toTransform.height.toFloat(),
            0f,
            0f,
            Color.BLACK,
            0x0,
            Shader.TileMode.MIRROR
        )
        val canvas = Canvas(toTransform)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.shader = linearGradient
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DARKEN)
        canvas.drawRect(0f, 0f, toTransform.width.toFloat(), toTransform.height.toFloat(), paint)
        return toTransform
    }
}