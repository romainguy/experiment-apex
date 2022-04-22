package dev.romainguy.apex

import android.graphics.RectF
import android.util.SizeF
import kotlin.math.max
import kotlin.math.min

val EmptyBounds = RectF()

fun min(a: SizeF, b: SizeF) = SizeF(
    min(a.width, b.width),
    min(a.height, b.height)
)

fun max(a: SizeF, b: SizeF) = SizeF(
    max(a.width, b.width),
    max(a.height, b.height)
)

fun Element.Row(elementPadding: Float = 8.0f, content: Element.() -> Unit = { }) = ChildElement {
    Layout { providers, element, size ->
        val density = providers.get<DensityProvider>()
        val elementOffset = density.toPx(elementPadding)

        val padding = element.componentOrNull<PaddingComponent>()?.padding ?: EmptyBounds

        var x = padding.left
        val y = padding.top
        var height = 0.0f

        element.forEachChild { child ->
            child.applyComponent<LayoutComponent> {
                val minSize = minSize(providers, child)
                val maxSize = maxSize(providers, child)

                val sizeConstraint = SizeF(
                    size.width - x - padding.right,
                    size.height - y - padding.bottom,
                )
                var childSize = layout(providers, child, sizeConstraint)
                childSize = min(max(minSize, childSize), maxSize)

                bounds = RectF(x, y, x + childSize.width, y + childSize.height)
                x += bounds.width() + elementOffset

                height = max(height, bounds.height())
            }
        }

        SizeF(
            max(0.0f, x - elementOffset) + padding.right,
            height + padding.top + padding.bottom
        )
    }

    content()
}

fun Element.Column(elementPadding: Float = 8.0f, content: Element.() -> Unit = { }) = ChildElement {
    Layout { providers, element, size ->
        val density = providers.get<DensityProvider>()
        val elementOffset = density.toPx(elementPadding)

        val padding = element.componentOrNull<PaddingComponent>()?.padding ?: EmptyBounds

        val x = padding.left
        var y = padding.top
        var width = 0.0f

        element.forEachChild { child ->
            child.applyComponent<LayoutComponent> {
                val minSize = minSize(providers, child)
                val maxSize = maxSize(providers, child)

                val sizeConstraint = SizeF(
                    size.width - x - padding.right,
                    size.height - y - padding.bottom,
                )
                var childSize = layout(providers, child, sizeConstraint)
                childSize = min(max(minSize, childSize), maxSize)

                bounds = RectF(x, y, x + childSize.width, y + childSize.height)
                y += bounds.height() + elementOffset

                width = max(width, bounds.width())
            }
        }

        SizeF(
            width + padding.left + padding.right,
            max(0.0f, y - elementOffset) + padding.bottom
        )
    }

    content()
}
