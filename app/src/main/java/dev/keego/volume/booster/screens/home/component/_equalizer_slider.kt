package dev.keego.volume.booster.screens.home.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import dev.keego.volume.booster.shared.ui._vertical_gradient_slider

@Composable
fun _equalizer_slider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    /*@IntRange(from = 0)*/
    steps: Int = 0,
    onValueChangeFinished: (() -> Unit)? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    colors: SliderColors = SliderDefaults.colors()
) {
//    Slider(
//        colors = colors,
//        interactionSource = interactionSource,
//        onValueChangeFinished = onValueChangeFinished,
//        steps = steps,
//        valueRange = valueRange,
//        enabled = enabled,
//        value = value,
//        onValueChange = onValueChange,
//        modifier = Modifier
//            .graphicsLayer {
//                rotationZ = 270f
//                transformOrigin = TransformOrigin(0f, 0f)
//            }
//            .layout { measurable, constraints ->
//                val placeable = measurable.measure(
//                    Constraints(
//                        minWidth = constraints.minHeight,
//                        maxWidth = constraints.maxHeight,
//                        minHeight = constraints.minWidth,
//                        maxHeight = constraints.maxHeight
//                    )
//                )
//                layout(placeable.height, placeable.width) {
//                    placeable.place(-placeable.width, 0)
//                }
//            }
//            .then(modifier)
//    )
    _vertical_gradient_slider(
        modifier = modifier
            .fillMaxSize(),
        value = value,
        displayValue = value / 100f,
        enable = enabled,
        range = valueRange,
        primaryColor = Color(0xFFFF5722),
        secondaryColor = Color(0xFF3FDB45),
        onValueChange = onValueChange
    )
}
