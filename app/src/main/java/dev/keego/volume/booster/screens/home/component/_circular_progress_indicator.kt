package dev.keego.volume.booster.screens.home.component

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import timber.log.Timber

// @Composable
// fun _old_circular_progress_indicator(
//    modifier: Modifier = Modifier,
//    initialValue: Int,
//    primaryColor: Color,
//    secondaryColor: Color,
//    range: IntRange = 0..100,
//    progressSize: Float,
//    circleRadius: Float,
//    onValueChange: (Int) -> Unit
// ) {
//    val totalAngle = 240f
//    var circleCenter by remember {
//        mutableStateOf(Offset.Zero)
//    }
//    var positionValue by remember {
//        mutableStateOf(initialValue)
//    }
//
//    var changeAngle by remember {
//        mutableStateOf(0f)
//    }
//
//    var dragStartedAngle by remember {
//        mutableStateOf(0f)
//    }
//
//    var oldPositionValue by remember {
//        mutableStateOf(initialValue)
//    }
//
//    Box(
//        modifier = modifier.size(width = progressSize.dp, height = progressSize.dp),
//        contentAlignment = Alignment.Center
//    ) {
//        Canvas(
//            modifier = modifier
//                .size(width = progressSize.dp, height = progressSize.dp)
//                .pointerInput(true) {
//                    detectDragGestures(
//                        onDragStart = { offset ->
//                            dragStartedAngle = -atan2(
//                                x = circleCenter.y - offset.y,
//                                y = circleCenter.x - offset.x
//                            ) * (180f / PI).toFloat()
//                            dragStartedAngle = (dragStartedAngle + (totalAngle / 2f))
//                        },
//                        onDrag = { change, _ ->
//                            var touchAngle = -atan2(
//                                x = circleCenter.y - change.position.y,
//                                y = circleCenter.x - change.position.x
//                            ) * (180f / PI).toFloat()
//                            touchAngle = (touchAngle + (totalAngle / 2f))
//                            changeAngle = touchAngle -
//                                oldPositionValue *
//                                totalAngle / (range.last - range.first)
//
//                            val currentAngle =
//                                oldPositionValue * totalAngle / ((range.last - range.first))
//                            changeAngle = touchAngle - currentAngle
//                            val lowerThreshold =
//                                currentAngle - totalAngle / (range.last - range.first) * 5
//                            val higherThreshold =
//                                currentAngle + totalAngle / (range.last - range.first) * 5
//
// //                            if (dragStartedAngle in lowerThreshold..higherThreshold) {
//                            val newPosition =
//                                (oldPositionValue + (changeAngle / (totalAngle / (range.last - range.first))).roundToInt())
//                            positionValue = if (newPosition in range) {
//                                Timber.d(
//                                    "circular log: join if $newPosition $oldPositionValue $changeAngle"
//                                )
//                                newPosition
//                            } else if (newPosition > range.last) {
//                                Timber.d(
//                                    "circular log: join else if $newPosition $oldPositionValue $changeAngle"
//                                )
//                                range.last
//                            } else {
//                                Timber.d(
//                                    "circular log: join else $newPosition $oldPositionValue $changeAngle"
//                                )
//                                range.first
//                            }
//                            onValueChange(positionValue)
// //                            }
// //                            Timber.d("circular log: $currentAngle $dragStartedAngle $lowerThreshold $higherThreshold")
//                        },
//                        onDragEnd = {
//                            oldPositionValue = positionValue
//                            onValueChange(positionValue)
//                        }
//                    )
//                }
//        ) {
//            var width = size.width
//            var height = size.height
//            val circleThickness = width / 25f
//            circleCenter = Offset(x = width / 2f, y = height / 2f)
//
//            drawCircle(
//                brush = Brush.radialGradient(
//                    listOf(primaryColor.copy(alpha = 0.45f), primaryColor.copy(alpha = 0.15f))
//                ),
//                radius = circleRadius,
//                center = circleCenter
//            )
//            // this is background of progress bar
//            drawCircle(
//                style = Stroke(
//                    width = circleThickness
//                ),
//                color = secondaryColor,
//                radius = circleRadius,
//                center = circleCenter
//            )
//
//            // this Arc is progress value ( rounded arc)
//            drawArc(
//                color = primaryColor,
//                startAngle = 150f,
//                sweepAngle = (totalAngle / range.last) * positionValue.toFloat(),
//                style = Stroke(
//                    width = circleThickness,
//                    cap = StrokeCap.Round
//                ),
//                useCenter = false,
//                size = Size(
//                    width = circleRadius * 2f,
//                    height = circleRadius * 2f
//                ),
//                topLeft = Offset(
//                    x = (width - circleRadius * 2f) / 2f,
//                    y = (height - circleRadius * 2f) / 2f
//                )
//            )
//
//            // litte line around circle
//            val outerRadius = circleRadius + circleThickness / 2f
//            val gap = 25f
//            for (i in 0 until (range.last - range.first)) {
//                val color = if (i < positionValue - range.first) {
//                    primaryColor
//                } else {
//                    primaryColor.copy(alpha = 0.3f)
//                }
//                val angleInDegrees = i * totalAngle / (range.last - range.first).toFloat()
//                val angleInRadian = angleInDegrees * PI / 180f + PI / 2f
//
//                val xGapAdjustment = -sin(angleInDegrees * PI / 180f) * gap
//                val yGapAdjustment = cos(angleInDegrees * PI / 180f) * gap
//
//                val start = Offset(
//                    x = (outerRadius * cos(angleInRadian) + circleCenter.x + xGapAdjustment).toFloat(),
//                    y = (outerRadius * sin(angleInRadian) + circleCenter.y + yGapAdjustment).toFloat()
//                )
//                val end = Offset(
//                    x = (outerRadius * cos(angleInRadian) + circleCenter.x + xGapAdjustment).toFloat(),
//                    y = (outerRadius * sin(angleInRadian) + circleThickness + circleCenter.y + yGapAdjustment).toFloat()
//                )
//                rotate(
//                    degrees = (360f - totalAngle) / 2
//                ) {
//                    rotate(
//                        degrees = angleInDegrees,
//                        pivot = start
//                    ) {
//                        drawLine(
//                            color = color,
//                            start = start,
//                            end = end,
//                            strokeWidth = 1.dp.toPx()
//                        )
//                    }
//                }
//            }
//            drawContext.canvas.nativeCanvas.apply {
//                drawIntoCanvas {
//                    drawText(
//                        "$positionValue %",
//                        circleCenter.x,
//                        circleCenter.y + 45.dp.toPx() / 3f,
//                        Paint().apply {
//                            textSize = 38.sp.toPx()
//                            textAlign = Paint.Align.CENTER
//                            color = Color.White.toArgb()
//                            isFakeBoldText = true
//                        }
//                    )
//                }
//            }
//        }
//    }
// }

@Composable
fun _circular_progress_indicator(
    modifier: Modifier = Modifier,
    value: Int, // <- Passed from outside
    primaryColor: Color,
    secondaryColor: Color,
    range: IntRange = 0..100,
    progressSize: Float,
    circleRadius: Float,
    onValueChange: (Int) -> Unit
) {
    val totalAngle = 240f
    var circleCenter by remember {
        mutableStateOf(Offset.Zero)
    }
//    var positionValue by remember {
//        mutableStateOf(initialValue)
//    }

    var changeAngle by remember {
        mutableStateOf(0f)
    }

    var dragStartedAngle by remember {
        mutableStateOf(0f)
    }

    var oldPositionValue by remember {
        mutableStateOf(value) // <- Use value instead of initialValue
    }

    Box(
        modifier = modifier.size(width = progressSize.dp, height = progressSize.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = modifier
                .size(width = progressSize.dp, height = progressSize.dp)
                .pointerInput(true) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            dragStartedAngle = -atan2(
                                x = circleCenter.y - offset.y,
                                y = circleCenter.x - offset.x
                            ) * (180f / PI).toFloat()
                            dragStartedAngle = (dragStartedAngle + (totalAngle / 2f))
                        },
                        onDrag = { change, _ ->
                            var touchAngle = -atan2(
                                x = circleCenter.y - change.position.y,
                                y = circleCenter.x - change.position.x
                            ) * (180f / PI).toFloat()
                            touchAngle = (touchAngle + (totalAngle / 2f))
                            changeAngle = touchAngle -
                                oldPositionValue *
                                totalAngle / (range.last - range.first)

                            val currentAngle =
                                oldPositionValue * totalAngle / ((range.last - range.first))
                            changeAngle = touchAngle - currentAngle
                            val lowerThreshold =
                                currentAngle - totalAngle / (range.last - range.first) * 5
                            val higherThreshold =
                                currentAngle + totalAngle / (range.last - range.first) * 5

//                            if (dragStartedAngle in lowerThreshold..higherThreshold) {
                            val newPosition =
                                (oldPositionValue + (changeAngle / (totalAngle / (range.last - range.first))).roundToInt())
//                            positionValue = if (newPosition in range) {
//                                Timber.d(
//                                    "circular log: join if $newPosition $oldPositionValue $changeAngle"
//                                )
//                                newPosition
//                            } else if (newPosition > range.last) {
//                                Timber.d(
//                                    "circular log: join else if $newPosition $oldPositionValue $changeAngle"
//                                )
//                                range.last
//                            } else {
//                                Timber.d(
//                                    "circular log: join else $newPosition $oldPositionValue $changeAngle"
//                                )
//                                range.first
//                            }
//                            onValueChange(positionValue)
                            onValueChange(
                                if (newPosition in range) newPosition else if (newPosition > range.last) range.last else range.first
                            )
//                            }
                            Timber.d(
                                "circular log: $currentAngle $dragStartedAngle $lowerThreshold $higherThreshold"
                            )
                        },
                        onDragEnd = {
//                            oldPositionValue = value // <- Use value instead of positionValue
//                            onValueChange(value)
                        }
                    )
                }
        ) {
            var width = size.width
            var height = size.height
            val circleThickness = width / 25f
            circleCenter = Offset(x = width / 2f, y = height / 2f)

            drawCircle(
                brush = Brush.radialGradient(
                    listOf(primaryColor.copy(alpha = 0.45f), primaryColor.copy(alpha = 0.15f))
                ),
                radius = circleRadius,
                center = circleCenter
            )
            // this is background of progress bar
            drawCircle(
                style = Stroke(
                    width = circleThickness
                ),
                color = secondaryColor,
                radius = circleRadius,
                center = circleCenter
            )

            // this Arc is progress value ( rounded arc)
            drawArc(
                color = primaryColor,
                startAngle = 150f,
                sweepAngle = (totalAngle / range.last) * value.toFloat(),
                style = Stroke(
                    width = circleThickness,
                    cap = StrokeCap.Round
                ),
                useCenter = false,
                size = Size(
                    width = circleRadius * 2f,
                    height = circleRadius * 2f
                ),
                topLeft = Offset(
                    x = (width - circleRadius * 2f) / 2f,
                    y = (height - circleRadius * 2f) / 2f
                )
            )

            // litte line around circle
            val outerRadius = circleRadius + circleThickness / 2f
            val gap = 25f
            for (i in 0 until 100) {
                val color = if (i*(range.last - range.first)/ 100f < value - range.first) {
                    primaryColor
                } else {
                    primaryColor.copy(alpha = 0.3f)
                }
                val angleInDegrees = i * totalAngle / 100.toFloat()
                val angleInRadian = angleInDegrees * PI / 180f + PI / 2f

                val xGapAdjustment = -sin(angleInDegrees * PI / 180f) * gap
                val yGapAdjustment = cos(angleInDegrees * PI / 180f) * gap

                val start = Offset(
                    x = (outerRadius * cos(angleInRadian) + circleCenter.x + xGapAdjustment).toFloat(),
                    y = (outerRadius * sin(angleInRadian) + circleCenter.y + yGapAdjustment).toFloat()
                )
                val end = Offset(
                    x = (outerRadius * cos(angleInRadian) + circleCenter.x + xGapAdjustment).toFloat(),
                    y = (outerRadius * sin(angleInRadian) + circleThickness + circleCenter.y + yGapAdjustment).toFloat()
                )
                rotate(
                    degrees = (360f - totalAngle) / 2
                ) {
                    rotate(
                        degrees = angleInDegrees,
                        pivot = start
                    ) {
                        drawLine(
                            color = color,
                            start = start,
                            end = end,
                            strokeWidth = 1.dp.toPx()
                        )
                    }
                }
            }
            drawContext.canvas.nativeCanvas.apply {
                drawIntoCanvas {
                    drawText(
                        "$value %",
                        circleCenter.x,
                        circleCenter.y + 45.dp.toPx() / 3f,
                        Paint().apply {
                            textSize = 38.sp.toPx()
                            textAlign = Paint.Align.CENTER
                            color = Color.White.toArgb()
                            isFakeBoldText = true
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun _circular_progress_preview() {
//    _old_circular_progress_indicator(
//        modifier = Modifier
//            .size(250.dp)
//            .background(Color(0xF1A1A1A)),
//        initialValue = 1,
//        primaryColor = Color(0xFFFF5722),
//        secondaryColor = Color.DarkGray,
//        progressSize = 250f,
//        circleRadius = 230f,
//        onValueChange = {}
//    )
}
