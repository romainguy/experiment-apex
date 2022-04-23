package dev.romainguy.apex.android

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import androidx.core.graphics.withTranslation
import dev.romainguy.apex.Point
import dev.romainguy.apex.Rect
import dev.romainguy.apex.Renderer

internal fun Rect.toRect(): android.graphics.Rect {
    return android.graphics.Rect(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
}

internal class AndroidRenderer(val canvas: Canvas) : Renderer {
    override var position: Point = Point(0f, 0f)

    override fun drawText(text: String, style: Paint) {
        canvas.drawText(text, position.x, position.y, style)
    }

    override fun drawBitmap(bitmap: Bitmap, style: Paint) {
        canvas.drawBitmap(bitmap, position.x, position.y, style)
    }

    override fun drawBitmap(bitmap: Bitmap, src: Rect, dst: Rect, style: Paint) {
        canvas.withTranslation(position.x, position.y) {
            drawBitmap(bitmap, src.toRect(), dst.toRect(), style)
        }
    }

    override fun drawRect(rect: Rect, style: Paint) {
        canvas.withTranslation(position.x, position.y) {
            drawRect(rect.left, rect.top, rect.right, rect.bottom, style)
        }
    }

    override fun drawRoundRect(rect: Rect, radius: Point, style: Paint) {
        canvas.withTranslation(position.x, position.y) {
            drawRoundRect(rect.left, rect.top, rect.right, rect.bottom, radius.x, radius.y, style)
        }
    }
}
