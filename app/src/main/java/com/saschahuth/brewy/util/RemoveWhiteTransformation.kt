package com.saschahuth.brewy.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import com.bumptech.glide.Glide
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.load.resource.bitmap.BitmapResource

/**
 * Created by sascha on 28.02.16.
 */
class RemoveWhiteTransformation(context: Context) : Transformation<Bitmap> {

    val context = context.applicationContext
    val bitmapPool = Glide.get(context).bitmapPool

    override fun transform(p0: Resource<Bitmap>?, p1: Int, p2: Int): Resource<Bitmap>? {

        val source = p0?.get()

        val bitmap = source?.copy(Bitmap.Config.ARGB_8888, true)
        if (bitmap != null) {
            val allPixels = IntArray(bitmap.width * bitmap.height)

            bitmap.getPixels(allPixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

            for (i in 0..allPixels.size - 1) {
                if (allPixels[i] == Color.WHITE) {
                    allPixels[i] = Color.TRANSPARENT
                }
            }

            bitmap.setPixels(allPixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        }

        source?.recycle()
        return BitmapResource.obtain(bitmap, bitmapPool)
    }

    override fun getId(): String? {
        return "RemoveWhiteTransformation"
    }

}