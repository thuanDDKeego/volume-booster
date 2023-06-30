package dev.keego.volume.booster.shared.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
@Stable
fun _vertical_gradient_slider(
    modifier: Modifier = Modifier,
    value: Float, // <- Passed from outside
    primaryColor: Color,
    secondaryColor: Color,
    enable: Boolean = true,
    numberOfOutlineLine: Int = 15,
    // range always start from 0 to maxValue
    range: ClosedFloatingPointRange<Float> = 0f..1f,
    trackWidth: Dp = 36.dp,
    onValueChange: (Float) -> Unit
) {
    val totalRange = range.endInclusive - range.start
    val primaryColor = if (enable) primaryColor else Color(0xFF555555)
    val secondaryColor = if (enable) secondaryColor else Color(0xFF2B2B2B)
    var circleCenter by remember {
        mutableStateOf(Offset.Zero)
    }
    val animateValue by animateFloatAsState(
        targetValue = value
    )

    Box(
        modifier = modifier
            .pointerInput(true) {
                if (enable) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            /* Called when the drag gesture starts */
                            // Called when the drag gesture is updated
                            val newValue =
                                (range.endInclusive - (offset.y / size.height) * totalRange)
                                    .coerceIn(range.start, range.endInclusive)
                            onValueChange(newValue)
                        },
                        onDragEnd = { /* Called when the drag gesture ends */ },
                        onDragCancel = { /* Called when the drag gesture is cancelled */ },
                        onDrag = { change, dragAmount ->
                            // Called when the drag gesture is updated
                            val newValue =
                                (range.endInclusive - (change.position.y / size.height) * totalRange)
                                    .coerceIn(range.start, range.endInclusive)
                            onValueChange(newValue)
                        }
                    )
                }
            }
            .padding(horizontal = 5.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = modifier
        ) {
            val width = size.width
            val height = size.height
            val trackWidth = trackWidth.value
            circleCenter = Offset(x = width / 2f, y = height / 2f)
            // this is background of slider
            drawLine(
                color = primaryColor.copy(alpha = 0.3f),
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
                lerp(secondaryColor, Color.Red, fraction.toFloat() / steps)
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
                    y = trackWidth / 2f + (height - trackWidth) * ((range.endInclusive - animateValue) / totalRange)
                ),
                strokeWidth = trackWidth,
                cap = StrokeCap.Round
            )
            val pointRadius = trackWidth
            drawCircle(
                color = primaryColor.copy(alpha = 0.3f),
                radius = trackWidth,
                center = Offset(
                    x = width / 2f,
                    y = trackWidth / 2f + (height - pointRadius) * ((range.endInclusive - animateValue) / totalRange)
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
        value = 0f,
        range = -100f..100f,
        primaryColor = Color(0xFFFF5722),
        secondaryColor = Color(0xFF3FDB45),
        onValueChange = {}
    )
}
