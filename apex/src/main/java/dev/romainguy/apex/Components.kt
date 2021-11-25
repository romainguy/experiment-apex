package dev.romainguy.apex

import android.graphics.Canvas
import android.graphics.RectF
import android.util.SizeF
import android.view.MotionEvent

val EmptySize = SizeF(0.0f, 0.0f)
val UnboundSize = SizeF(Float.MAX_VALUE, Float.MAX_VALUE)

interface RenderComponent {
    fun render(providers: Providers, element: Element, canvas: Canvas)
}

fun Element.Render(render: (providers: Providers, element: Element, canvas: Canvas) -> Unit) {
    addComponent(object : RenderComponent {
        override fun render(providers: Providers, element: Element, canvas: Canvas) {
            render(providers, element, canvas)
        }
    })
}

abstract class LayoutComponent {
    var bounds = RectF()

    abstract fun layout(providers: Providers, element: Element, size: SizeF): SizeF

    fun minSize(providers: Providers, element: Element) = EmptySize
    fun maxSize(providers: Providers, element: Element) = UnboundSize
}

fun Element.Layout(layout: LayoutComponent) {
    addComponent(layout)
}

fun Element.Layout(layout: (providers: Providers, element: Element, size: SizeF) -> SizeF) {
    addComponent(object : LayoutComponent() {
        override fun layout(providers: Providers, element: Element, size: SizeF): SizeF {
            return layout(providers, element, size)
        }

    })
}

interface MotionInputComponent {
    fun motionInput(providers: Providers, element: Element, event: MotionEvent): Boolean
}

fun Element.MotionInput(
    action: (providers: Providers, element: Element, event: MotionEvent) -> Boolean
) {
    addComponent(object : MotionInputComponent {
        override fun motionInput(providers: Providers, element: Element, event: MotionEvent): Boolean {
            return action(providers, element, event)
        }
    })
}

interface ProviderComponent {
    fun provide(providers: Providers, element: Element)
}

inline fun <reified T : Any> Element.Provider(localProvider: T) {
    addComponent(object : ProviderComponent {
        override fun provide(providers: Providers, element: Element) {
            providers.set(localProvider)
        }
    })
}

class PaddingComponent(val padding: RectF) {
    constructor(padding: Float) : this(RectF(padding, padding, padding, padding))
}

fun Element.Padding(padding: RectF) {
    addComponent(PaddingComponent(padding))
}

fun Element.Padding(padding: Float) {
    addComponent(PaddingComponent(padding))
}

enum class VerticalAlignment {
    Start,
    Center,
    End
}

enum class HorizontalAlignment {
    Start,
    Center,
    End
}

fun Element.Alignment(alignment: VerticalAlignment) {
    addComponent(alignment)
}

fun Element.Alignment(alignment: HorizontalAlignment) {
    addComponent(alignment)
}
