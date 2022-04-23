package dev.romainguy.apex

import android.graphics.Bitmap
import android.graphics.Paint

interface Renderer {
    // TODO: abstract Paint into platform-agnostic rendering style
    var position: Point

    fun move(x: Float, y: Float) {
        position = Point(position.x + x, position.y + y)
    }

    fun drawText(text: String, style: Paint)

    // TODO: abstract Bitmap into platform-agnostic image object
    fun drawBitmap(bitmap: Bitmap, style: Paint)
    fun drawBitmap(bitmap: Bitmap, src: Rect, dst: Rect, style: Paint)
    fun drawRect(rect: Rect, style: Paint)
    fun drawRoundRect(rect: Rect, radius: Point, style: Paint)
}

data class Point(val x: Float, val y: Float)

data class Rect(val left: Float, val top: Float, val right: Float, val bottom: Float)

fun Rect(left: Int, top: Int, right: Int, bottom: Int) =
    Rect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat())

fun Rect(left: Int, top: Int, right: Float, bottom: Float) =
    Rect(left.toFloat(), top.toFloat(), right, bottom)
