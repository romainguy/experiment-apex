package dev.romainguy.apex

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.PointF
import android.graphics.RectF
import android.util.SizeF
import android.view.*
import androidx.core.graphics.contains
import androidx.core.graphics.withTranslation

private fun layout(providers: Providers, element: Element, size: SizeF) {
    element.child { child ->
        val localProviders = providers.copyOf()

        child.component<ProviderComponent> {
            provide(localProviders, child)
        }

        child.component<LayoutComponent> {
            val minSize = minSize(localProviders, child)
            val maxSize = maxSize(localProviders, child)
            val childSize = min(max(minSize, layout(localProviders, child, size)), maxSize)
            val halign = child.componentOrNull() ?: HorizontalAlignment.Start
            val valign = child.componentOrNull() ?: VerticalAlignment.Start

            val x = when (halign) {
                HorizontalAlignment.Start -> 0.0f
                HorizontalAlignment.Center -> (size.width - childSize.width) * 0.5f
                HorizontalAlignment.End -> size.width - childSize.width
            }
            val y = when (valign) {
                VerticalAlignment.Start -> 0.0f
                VerticalAlignment.Center -> (size.height - childSize.height) * 0.5f
                VerticalAlignment.End -> size.height - childSize.height
            }

            bounds = RectF(x, y, x + childSize.width, y + childSize.height)
        }
    }
}

private fun draw(providers: Providers, element: Element, canvas: Canvas) {
    element.child { child ->
        val localProviders = providers.copyOf()

        child.component<ProviderComponent> {
            provide(localProviders, child)
        }

        val bounds = child.componentOrNull<LayoutComponent>()?.bounds ?: EmptyBounds
        canvas.withTranslation(bounds.left, bounds.top) {
            child.component<RenderComponent> {
                render(localProviders, child, canvas)
            }

            draw(localProviders, child, canvas)
        }
    }
}

private fun motion(
    providers: Providers,
    element: Element,
    event: MotionEvent,
    x: Float,
    y: Float
): Boolean {
    var done = false

    element.child { child ->
        val localProviders = providers.copyOf()

        child.component<ProviderComponent>{
            provide(localProviders, child)
        }

        val sourceBounds = child.componentOrNull<LayoutComponent>()?.bounds ?: EmptyBounds
        val bounds = RectF(sourceBounds).apply {  offset(x, y) }

        if (PointF(event.x, event.y) in bounds) {
            child.component<MotionInputComponent> {
                if (motionInput(providers, child, event)) {
                    done = true
                }
            }
            if (!done) {
                done = motion(providers, child, event, x + sourceBounds.left, y + sourceBounds.top)
            }
        }

        if (done) {
            return@child
        }
    }

    return done
}

private class RootElement(context: Context) : Element() {
    private val providers = Providers()

    var size: SizeF = SizeF(0.0f, 0.0f)

    private var surface: SurfaceHolder? = null

    private val frame = object : Choreographer.FrameCallback {
        override fun doFrame(frameTimeNanos: Long) {
            if (surface == null) return

            val rootProviders = providers.copyOf()
            component<ProviderComponent>{
                provide(rootProviders, this@RootElement)
            }

            layout(rootProviders, this@RootElement, size)

            with (surface?.lockHardwareCanvas()!!) {
                drawColor(rootProviders.get<ThemeProvider>().background.toArgb())
                draw(rootProviders, this@RootElement, this)
                surface?.unlockCanvasAndPost(this)
            }

            Choreographer.getInstance().postFrameCallback(this)
        }
    }

    init {
        providers.set(DisplayProvider(context.resources.displayMetrics))
        providers.set(DensityProvider(context.resources.displayMetrics.density))
        providers.set(ResourcesProvider(context.resources))
        providers.set(ThemeProvider())
    }

    fun start(holder: SurfaceHolder) {
        surface = holder
        Choreographer.getInstance().postFrameCallback(frame)
    }

    fun stop() {
        Choreographer.getInstance().removeFrameCallback(frame)
        surface = null
    }

    fun onMotion(event: MotionEvent): Boolean {
        val rootProviders = providers.copyOf()
        component<ProviderComponent>{
            provide(rootProviders, this@RootElement)
        }

        return motion(rootProviders, this, event, 0.0f, 0.0f)
    }
}

@SuppressLint("ClickableViewAccessibility")
fun Activity.setContent(content: Element.() -> Unit) {
    val root = RootElement(this)
    root.content()

    val surface = SurfaceView(this)
    setContentView(surface)

    surface.setOnTouchListener { _, event ->
        root.onMotion(event!!)
        true
    }

    surface.holder.addCallback(object : SurfaceHolder.Callback {
        override fun surfaceCreated(holder: SurfaceHolder) {
            root.start(holder)
        }

        override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            root.size = SizeF(width.toFloat(), height.toFloat())
        }

        override fun surfaceDestroyed(holder: SurfaceHolder) {
            root.stop()
        }
    })
}
