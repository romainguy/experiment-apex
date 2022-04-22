package dev.romainguy.apex

import android.graphics.Bitmap
import android.graphics.Paint

interface Renderer {
    // TODO: abstract Paint into platform-agnostic rendering style
    var style: Paint
    var position: Point

    fun move(x: Float, y: Float) {
        position = Point(position.x + x, position.y + y)
    }

    fun drawText(text: String)

    // TODO: abstract Bitmap into platform-agnostic image object
    fun drawBitmap(bitmap: Bitmap)
    fun drawBitmap(bitmap: Bitmap, src: Rect, dst: Rect)
    fun drawRect(rect: Rect)
    fun drawRoundRect(rect: Rect, radius: Point)
}



data class Point(val x: Float, val y: Float)

data class Rect(val left: Float, val top: Float, val right: Float, val bottom: Float)

fun Rect(left: Int, top: Int, right: Int, bottom: Int) =
    Rect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat())
