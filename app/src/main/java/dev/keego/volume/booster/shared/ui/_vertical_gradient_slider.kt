package dev.keego.volume.booster.shared.ui

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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun _vertical_gradient_slider(
    modifier: Modifier = Modifier,
    value: Int, // <- Passed from outside
    primaryColor: Color,
    secondaryColor: Color,
    enable: Boolean = true,
    numberOfOutlineLine: Int = 15,
    // range always start from 0 to maxValue
    maxValue: Int = 100,
    progressSize: Float,
    circleRadius: Float,
    onValueChange: (Int) -> Unit
) {
    val primaryColor = if (enable) primaryColor else Color(0xFF555555)
    val secondaryColor = if (enable) secondaryColor else Color(0xFF2B2B2B)
    val range: IntRange = 0..maxValue
    var circleCenter by remember {
        mutableStateOf(Offset.Zero)
    }
    val animateValue by animateFloatAsState(
        targetValue = value.toFloat()
    )

    Box(
        modifier = modifier.size(width = progressSize.dp, height = progressSize.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = modifier
                .size(width = progressSize.dp, height = progressSize.dp)
                .pointerInput(true) {
                    if (enable) {
//                        forEachGesture {
//                            awaitPointerEventScope {
//                                val down = awaitFirstDown(requireUnconsumed = false)
//                                var oldValue = oldValue
//                                while (true) {
//                                    val change = awaitVerticalDragOrCancellation(down.id) ?: break
//                                    val newValue =
//                                        ((maxValue - (change.position.y / size.height) * maxValue).roundToInt()).coerceIn(
//                                            range
//                                        )
//                                    if (oldValue != newValue) {
//                                        onValueChange(newValue)
//                                        oldValue = newValue
//                                    }
//                                }
//                            }
//                        }
                        detectDragGestures(
                            onDragStart = { offset ->
                                /* Called when the drag gesture starts */
                                // Called when the drag gesture is updated
                                val newValue =
                                    (maxValue - (offset.y / size.height) * maxValue).toInt()
                                        .coerceIn(0, maxValue)
                                onValueChange(newValue)
                            },
                            onDragEnd = { /* Called when the drag gesture ends */ },
                            onDragCancel = { /* Called when the drag gesture is cancelled */ },
                            onDrag = { change, dragAmount ->
                                // Called when the drag gesture is updated
                                val newValue =
                                    (maxValue - (change.position.y / size.height) * maxValue).toInt()
                                        .coerceIn(0, maxValue)
                                onValueChange(newValue)
                            }
                        )
                    }
                }
                .padding(horizontal = 5.dp)
        ) {
            val width = size.width
            val height = size.height
            val circleThickness = width / 10f
            circleCenter = Offset(x = width / 2f, y = height / 2f)
//             this is background of progress bar
            drawLine(
                color = primaryColor.copy(alpha = 0.3f),
                start = Offset(
                    x = width / 2f,
                    y = circleThickness / 2f
                ),
                end = Offset(
                    x = width / 2f,
                    y = height - circleThickness / 2f
                ),
                strokeWidth = circleThickness,
                cap = StrokeCap.Round
            )
            val steps = 100
            val gradientColors = List(steps) { fraction ->
                lerp(secondaryColor, Color.Red, fraction.toFloat() / steps)
            }
            drawLine(
                brush = Brush.verticalGradient(
                    listOf(
                        gradientColors[(100 - (value / maxValue)).coerceIn(0, 99)],
                        secondaryColor
                    )
                ),
                start = Offset(
                    x = width / 2f,
                    y = height - circleThickness / 2f
                ),
                end = Offset(
                    x = width / 2f,
                    y = circleThickness / 2f + (height - circleThickness) * ((range.last - animateValue.toFloat()) / range.last)
                ),
                strokeWidth = circleThickness,
                cap = StrokeCap.Round
            )
            val pointRadius = circleThickness
            drawCircle(
                color = primaryColor.copy(alpha = 0.3f),
                radius = circleThickness,
                center = Offset(
                    x = width / 2f,
                    y = circleThickness / 2f + (height - pointRadius) * ((range.last - animateValue.toFloat()) / range.last)
                )

            )
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
        value = 75,
        maxValue = 100,
        primaryColor = Color(0xFFFF5722),
        secondaryColor = Color(0xFF3FDB45),
        progressSize = 250f,
        circleRadius = 230f,
        onValueChange = {}
    )
}
