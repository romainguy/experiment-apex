package dev.romainguy.apex

import android.content.res.Resources
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.util.DisplayMetrics
import kotlin.collections.MutableMap
import kotlin.collections.mutableMapOf
import kotlin.collections.set
import kotlin.math.floor
import kotlin.reflect.KClass

class Providers {
    private val providers: MutableMap<KClass<*>, Any> = mutableMapOf()

    inline fun <reified T : Any> get(): T {
        return this[T::class]
    }

    @Suppress("UNCHECKED_CAST")
    operator fun <T : Any> get(type: KClass<T>) = providers[type] as T

    inline fun <reified T: Any> set(provider: T) {
        this[T::class] = provider
    }

    operator fun <T: Any> set(type: KClass<T>, provider: T) {
        providers[type] = provider
    }

    fun copyOf(): Providers {
        val p = Providers()
        p.providers.putAll(providers)
        return p
    }
}

class DensityProvider(val density: Float) {
    fun toPx(v: Float) = floor((v * density) + 0.5f)
    fun toPx(r: RectF) = RectF(toPx(r.left), toPx(r.top), toPx(r.right), toPx(r.bottom))
}

context(DensityProvider)
@Suppress("NOTHING_TO_INLINE")
inline fun Float.toPx() = toPx(this)

context(DensityProvider)
@Suppress("NOTHING_TO_INLINE")
inline fun RectF.toPx() = toPx(this)

class DisplayProvider(val display: DisplayMetrics)

class ResourcesProvider(val resources: Resources)

class ThemeProvider(
    val border: Color = Color.valueOf(0.25f, 0.53f, 0.94f),
    val background: Color = Color.valueOf(1.0f, 1.0f, 1.0f),
    val contentBackground: Color = Color.valueOf(0.75f, 0.85f, 0.95f),
    val disabled: Color = border.desaturated(),
    val contentDisabled: Color = Color.valueOf(0.92f, 0.92f, 0.92f),
    val text: Color = Color.valueOf(0.25f, 0.53f, 0.94f, 0.8f),
    val fontSize: Float = 18.0f,
    val typeface: Typeface = Typeface.create("sans-serif-condensed", Typeface.NORMAL),
    val style: Paint.Style = Paint.Style.STROKE,
    val cornerRadius: Float = 6.0f,
    val strokeWidth: Float = 1.5f
)
