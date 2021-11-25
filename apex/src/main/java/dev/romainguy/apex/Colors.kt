package dev.romainguy.apex

import android.graphics.Color
import androidx.core.graphics.*
import kotlin.math.min

fun Color.complementary(): Color {
    val (_, r, g, b) = toArgb()
    val out = FloatArray(3)
    ColorUtils.RGBToHSL(r, g, b, out)
    out[0] = (out[0] + 180.0f) % 360.0f
    return Color.valueOf(ColorUtils.HSLToColor(out))
}

fun Color.desaturated(): Color {
    val (_, r, g, b) = toArgb()
    val out = FloatArray(3)
    ColorUtils.RGBToHSL(r, g, b, out)
    out[1] = min(out[1] * 0.1f, 1.0f)
    return Color.valueOf(ColorUtils.HSLToColor(out))
}
