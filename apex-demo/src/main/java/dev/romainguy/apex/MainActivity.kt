package dev.romainguy.apex

import android.app.Activity
import android.graphics.*
import android.os.Bundle
import android.util.SizeF

enum class Id {
    ButtonNext,
    ButtonPrevious
}

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadTestImages(this)

        var imageIndex = 0

        setContent {

// Simpler, non-interactive version of the same UI:
//            Column {
//                Alignment(HorizontalAlignment.Center)
//                Alignment(VerticalAlignment.Center)
//
//                Padding(16.0f)
//
//                Image(ImageModel(TestImage[0]))
//
//                Row {
//                    Button(ButtonModel("Previous", State.Disabled))
//                    Button(ButtonModel("Next"))
//                }
//            }

// Example of how to use providers to override global resources down the tree.
// Providers can be set at any level:
//            Provider(ThemeProvider(
//                style = Paint.Style.FILL_AND_STROKE,
//                border = Color.valueOf(0.46f, 0.81f, 0.57f),
//                contentBackground = Color.valueOf(0.91f, 0.96f, 0.90f),
//                disabled = Color.valueOf(0.46f, 0.81f, 0.57f).desaturated(),
//                text = Color.valueOf(0.46f, 0.81f, 0.57f),
//                typeface = Typeface.create(
//                    Typeface.create("sans-serif-condensed", Typeface.NORMAL),
//                    200,
//                    false
//                ),
//                strokeWidth = 1.0f
//            ))

            Column {
                Alignment(HorizontalAlignment.Center)
                Alignment(VerticalAlignment.Center)

                Padding(16.0f)

                val image = Image(ImageModel(TestImage[imageIndex]))

                Row {
                    Button(ButtonModel("Previous", State.Disabled) { previous ->
                        if (imageIndex > 0) imageIndex--
                        requireChild(Id.ButtonNext).component<ButtonModel>().state =
                            if (imageIndex < TestImage.size) State.Enabled else State.Disabled
                        previous.component<ButtonModel>().state =
                            if (imageIndex > 0) State.Enabled else State.Disabled
                        image.component<ImageModel>().bitmap = TestImage[imageIndex]
                    }).addComponent(Id.ButtonPrevious)

                    Button(ButtonModel("Next") { next ->
                        if (imageIndex < TestImage.size - 1) imageIndex++
                        next.component<ButtonModel>().state =
                            if (imageIndex < TestImage.size - 1) State.Enabled else State.Disabled
                        requireChild(Id.ButtonPrevious).component<ButtonModel>().state =
                            if (imageIndex > 0) State.Enabled else State.Disabled
                        image.component<ImageModel>().bitmap = TestImage[imageIndex]
                    }).addComponent(Id.ButtonNext)
                }
            }
        }
    }
}

// Example of creating your own widget. The code below uses convenience functions but it could
// be written as:
// val element = Element()
//     .addComponent(model)
//     .addComponent(object : LayoutComponent { … })
//     .addComponent(object : RenderComponent { … })

class ImageModel(var bitmap: Bitmap)

fun Element.Image(model: ImageModel) = ChildElement {
    addComponent(model)

    val paint = Paint().apply {
        isFilterBitmap = true
    }

    Layout { _, element, size ->
        val bitmap = element.component<ImageModel>().bitmap
        var width = bitmap.width.toFloat()
        var height = bitmap.height.toFloat()

        val scale = if (width * size.height < size.width * height) {
            size.height / height
        } else {
            size.width / width
        }

        width *= scale
        height *= scale

        SizeF(width, height)
    }

    Render { _, element, canvas ->
        val bitmap = element.component<ImageModel>().bitmap
        val bounds = element.component<LayoutComponent>().bounds
        canvas.drawBitmap(
            bitmap,
            Rect(0, 0, bitmap.width, bitmap.height),
            RectF(0.0f, 0.0f, bounds.width(), bounds.height()),
            paint
        )
    }
}
