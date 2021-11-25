package dev.romainguy.apex

import android.content.Context
import android.graphics.BitmapFactory
import androidx.core.graphics.createBitmap

val TestImage = Array(3) { createBitmap(1, 1) }

fun loadTestImages(context: Context) {
    TestImage[0] = BitmapFactory.decodeResource(context.resources, R.drawable.tokyo)
    TestImage[1] = BitmapFactory.decodeResource(context.resources, R.drawable.mountains)
    TestImage[2] = BitmapFactory.decodeResource(context.resources, R.drawable.car)
}
