package dev.romainguy.apex.android

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import dev.romainguy.apex.Point
import dev.romainguy.apex.Rect
import dev.romainguy.apex.Renderer

fun Rect.toRect(): android.graphics.Rect {
    return android.graphics.Rect(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
}

class AndroidRenderer(val canvas: Canvas) : Renderer {
    override var position: Point
        get() = Point(0f, 0f)
        set(value) = canvas.translate(value.x, value.y)

    override fun drawText(text: String, style: Paint) {
        canvas.drawText(text, position.x, position.y, style)
    }

    override fun drawBitmap(bitmap: Bitmap, style: Paint) {
        canvas.drawBitmap(bitmap, position.x, position.y, style)
    }

    override fun drawBitmap(bitmap: Bitmap, src: Rect, dst: Rect, style: Paint) {
        canvas.drawBitmap(bitmap, src.toRect(), dst.toRect(), style)
    }

    override fun drawRect(rect: Rect, style: Paint) {
        canvas.drawRect(rect, style)
    }

    override fun drawRoundRect(rect: Rect, radius: Point, style: Paint) {
        canvas.drawRoundRect(rect, radius, style)
    }
}

private fun Canvas.drawBitmap(bitmap: Bitmap, src: Rect, dst: Rect, paint: Paint) {
    drawBitmap(bitmap, src.toRect(), dst.toRect(), paint)
}

private fun Canvas.drawRoundRect(rect: Rect, radius: Point, paint: Paint) {
    drawRoundRect(rect.left, rect.top, rect.right, rect.bottom, radius.x, radius.y, paint)
}

private fun Canvas.drawRect(rect: Rect, paint: Paint) {
    drawRect(rect.left, rect.top, rect.right, rect.bottom, paint)
}
