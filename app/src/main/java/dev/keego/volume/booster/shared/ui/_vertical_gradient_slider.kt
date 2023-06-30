package dev.keego.volume.booster.shared.ui

import android.graphics.Typeface
import android.graphics.drawable.Drawable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import dev.keego.volume.booster.R
import kotlin.math.roundToInt

@Composable
fun _vertical_gradient_slider(
    modifier: Modifier = Modifier,
    value: Float, // <- Passed from outside,
    displayValue: Float = value,
    primaryColor: Color,
    secondaryColor: Color,
    enable: Boolean = true,
    numberOfLevelLine: Int = 17,
    range: ClosedFloatingPointRange<Float> = 0f..1f,
    trackWidth: Dp = 36.dp,
    thumbSize: Size = Size(28f, 16f),
    onValueChange: (Float) -> Unit
) {
    val context = LocalContext.current
    val totalRange = range.endInclusive - range.start
    val primaryColor = if (enable) primaryColor else Color(0xFF555555)
    val secondaryColor = if (enable) secondaryColor else Color(0xFF2B2B2B)
    var circleCenter by remember {
        mutableStateOf(Offset.Zero)
    }
    var isDragging by remember { mutableStateOf(false) }

    val textLayoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }
    val valueText = remember { mutableStateOf("") }

    val animateValue by animateFloatAsState(
        targetValue = value
    )
    val sliderControlDrawable: Drawable? =
        ContextCompat.getDrawable(context, R.drawable.ic_slider_control)
    val sliderControlBitmap = sliderControlDrawable?.toBitmap()?.asImageBitmap()

    // this modifier handle user's dragger
    var boxModifier = modifier
    if (enable) {
        boxModifier = boxModifier.pointerInput(true) {
            if (enable) {
                val dragCircleColor = Color.Black.copy(alpha = 0.2f) // adjust as needed
                val dragCircleStrokeColor = Color.Yellow // adjust as needed
                detectDragGestures(
                    onDragStart = { offset ->
                        /* Called when the drag gesture starts */
                        // Called when the drag gesture is updated
                        if (enable) {
                            isDragging = true
                            val newValue =
                                (range.endInclusive - (offset.y / size.height) * totalRange)
                                    .coerceIn(range.start, range.endInclusive)
                            onValueChange(newValue)
                        }
                    },
                    onDragEnd = {
                        /* Called when the drag gesture ends */
                        isDragging = false
                    },
                    onDragCancel = {
                        /* Called when the drag gesture is cancelled */
                        isDragging = false
                    },
                    onDrag = { change, dragAmount ->
                        // Called when the drag gesture is updated
                        if (enable) {
                            isDragging = true
                            val newValue =
                                (
                                    range.endInclusive - (change.position.y / size.height) *
                                        totalRange
                                    )
                                    .coerceIn(range.start, range.endInclusive)
                            onValueChange(newValue)
                        }
                    }
                )
            }
        }
    }

    Box(
        modifier = boxModifier
            .padding(horizontal = 5.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = modifier
        ) {
            val width = size.width
            val height = size.height

            //region level lines
            val trackWidth = trackWidth.value
            circleCenter = Offset(x = width / 2f, y = height / 2f)

            val regularLineLength = 10f // The length of most lines
            val specialLineLength = 18f // The length of the first, last, and middle lines
            val lineWidth = 2f // The thickness of each line
            val lineColor = Color.DarkGray.copy(alpha = 0.8f) // The color of the lines
            // The vertical distance between each line
            val lineSpacing = height / (numberOfLevelLine - 1)

            for (i in 0 until numberOfLevelLine) {
                // The vertical position of the line
                val yPos = i * lineSpacing
                val lineLength = when (i) {
                    0, numberOfLevelLine - 1, numberOfLevelLine / 2 -> specialLineLength
                    else -> regularLineLength
                }

                // Draw the line on the left side of the slider
                drawLine(
                    color = lineColor,
                    start = Offset(x = width / 2f - trackWidth / 2f - lineLength, y = yPos),
                    end = Offset(x = width / 2f - trackWidth / 2f - 4f, y = yPos),
                    strokeWidth = lineWidth,
                    cap = StrokeCap.Round
                )

                // Draw the line on the right side of the slider
                drawLine(
                    color = lineColor,
                    start = Offset(x = width / 2f + trackWidth / 2f + 4f, y = yPos),
                    end = Offset(x = width / 2f + trackWidth / 2f + lineLength, y = yPos),
                    strokeWidth = lineWidth,
                    cap = StrokeCap.Round
                )
            }
            //endregion
            // this is background of slider
            drawLine(
                brush = Brush.horizontalGradient(
                    listOf(
                        Color(0xFF0A0A0A),
                        Color(0xFF383838)
                    ),
                    startX = width / 2f - trackWidth / 2f,
                    endX = width / 2f + trackWidth / 2f
                ),
                start = Offset(
                    x = width / 2f,
                    y = trackWidth / 2f
                ),
                end = Offset(
                    x = width / 2f,
                    y = height - trackWidth / 2f
                ),
                strokeWidth = trackWidth,
                cap = StrokeCap.Round
            )
            val steps = 100
            val gradientColors = List(steps) { fraction ->
                lerp(secondaryColor, primaryColor, fraction.toFloat() / steps)
            }
            drawLine(
                brush = Brush.verticalGradient(
                    listOf(
                        gradientColors[
                            (100 - ((range.endInclusive - value) / totalRange) * 100).roundToInt()
                                .coerceIn(0, 99)
                        ],
                        secondaryColor
                    )
                ),
                start = Offset(
                    x = width / 2f,
                    y = height - trackWidth / 2f
                ),
                end = Offset(
                    x = width / 2f,
                    y = trackWidth / 2f +
                        (height - trackWidth) * ((range.endInclusive - animateValue) / totalRange)
                ),
                strokeWidth = trackWidth,
                cap = StrokeCap.Round
            )
            val pointRadius = trackWidth
//            drawCircle(
//                color = primaryColor.copy(alpha = 0.3f),
//                radius = trackWidth,
//                center = Offset(
//                    x = width / 2f,
//                    y = trackWidth / 2f +
//                        (height - pointRadius) * ((range.endInclusive - animateValue) / totalRange)
//                )
//            )

            drawImage(
                image = sliderControlBitmap!!,
                topLeft = Offset(
                    x = width / 2f - sliderControlBitmap.width / 2f,
                    y = trackWidth / 2f +
                        (height - pointRadius) * ((range.endInclusive - animateValue) / totalRange) -
                        sliderControlBitmap.height / 2f
                )
            )
            // region circle display value
            // The drag circle
            if (isDragging) {
                val dragCircleRadius = 50f // adjust as needed
                val dragCircleStrokeWidth = 8f // adjust as needed
                val dragCircleColor = Color(0xFFFF9800) // adjust as needed
                val dragCircleStrokeColor = Color.Black // adjust as needed
                val dragCircleY = (
                    trackWidth / 2f +
                        (height - trackWidth) * ((range.endInclusive - animateValue) / totalRange) -
                        dragCircleRadius * 2 - 10f
                    )
                drawCircle(
                    color = dragCircleColor,
                    center = Offset(x = width / 2f, y = dragCircleY),
                    radius = dragCircleRadius,
                    style = Stroke(
                        width = dragCircleStrokeWidth,
                        cap = StrokeCap.Round
                    )
                )

                // The value text inside the drag circle
                val textPaint = Paint().asFrameworkPaint().apply {
                    color = android.graphics.Color.BLACK // adjust as needed
                    textSize = 14f // adjust as needed
                    textAlign = android.graphics.Paint.Align.CENTER
                    typeface = Typeface.DEFAULT_BOLD
                }
                drawContext.canvas.nativeCanvas.apply {
                    drawIntoCanvas {
                        drawText(
                            "${displayValue.roundToInt()}",
                            width / 2f,
                            dragCircleY + 14f,
                            android.graphics.Paint().apply {
                                textSize = 14.sp.toPx()
                                textAlign = android.graphics.Paint.Align.CENTER
                                color = Color.White.toArgb()
                                isFakeBoldText = true
                            }
                        )
                    }
                }
            }
            //endregion
        }
    }
}

@Preview
@Composable
fun _circular_progress_preview() {
    _vertical_gradient_slider(
        modifier = Modifier
            .size(250.dp)
            .background(Color(0xF1A1A1A)),
        value = 15f,
        displayValue = 15f,
        range = -100f..100f,
        primaryColor = Color(0xFFFF5722),
        secondaryColor = Color(0xFF3FDB45),
        onValueChange = {}
    )
}
