package dev.keego.volume.booster.screens.home.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlin.math.abs

@Composable
fun _bar_visualizer(
    modifier: Modifier,
    resolution: Int = 32,
    visualizationData: Int
) {
    var size by remember { mutableStateOf(IntSize.Zero) }
    val gradientColors = List(100) { fraction ->
        lerp(
            MaterialTheme.colorScheme.secondary,
            Color.Red,
            fraction.toFloat() / 100
        )
    }
    Box(
        modifier
            .onSizeChanged { size = it }
            .clip(MaterialTheme.shapes.extraSmall)
            .background(Color(0x403D3D3D))
            .border(
                width = (1.5).dp,
                brush = Brush.verticalGradient(
                    listOf(
                        Color.Black,
                        Color(0xFF474747)
                    )
                ),
                shape = MaterialTheme.shapes.extraSmall
            )
            .padding(1.dp)
            .border(
                width = 5.dp,
                color = Color(0xFF181818),
                shape = MaterialTheme.shapes.extraSmall
            )
            .clip(
                shape = MaterialTheme.shapes.extraSmall
            )
    ) {
        val widthDp = size.getWidthDp()
        val heightDp = size.getHeightDp()
        val padding = 1.dp
        val barWidthDp = widthDp / resolution

//        visualizationData.forEachIndexed { index, data ->
        val height by animateDpAsState(
            targetValue = heightDp * abs(128f - visualizationData) / 128f
        )
        val visualizeGradient = listOf(
            gradientColors[(visualizationData / 128f * 100).toInt().coerceIn(0, 99)],
            gradientColors.first()
        )
        Box(
            Modifier
                .width(barWidthDp)
                .height(height)
//                    .padding(start = if (index == 0) 0.dp else padding)
                .background(
                    brush = Brush.verticalGradient(
                        visualizeGradient
                    )
                )
                .align(Alignment.BottomCenter)
        )
        Spacer(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxHeight()
                .width(5.dp)
                .background(Color(0xFF181818))
        )
        Column(modifier = Modifier.fillMaxSize()) {
            for (i in 0..18) {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(5.dp)
                        .background(Color(0xFF181818))
                )
                if (i != 18) {
                    Spacer(
                        modifier = Modifier
                            .weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun IntSize.getWidthDp(): Dp = LocalDensity.current.run { width.toDp() }

@Composable
fun IntSize.getHeightDp(): Dp = LocalDensity.current.run { height.toDp() }
