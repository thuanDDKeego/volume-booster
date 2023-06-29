package dev.keego.volume.booster.screens.home.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    Row(modifier.onSizeChanged { size = it }) {
        val widthDp = size.getWidthDp()
        val heightDp = size.getHeightDp()
        val padding = 1.dp
        val barWidthDp = widthDp / resolution

//        visualizationData.forEachIndexed { index, data ->
        val height by animateDpAsState(
            targetValue = heightDp * abs(128f - visualizationData) / 128f
        )
        Box(
            Modifier
                .width(barWidthDp)
                .height(height)
//                    .padding(start = if (index == 0) 0.dp else padding)
                .background(MaterialTheme.colorScheme.primary)
                .align(Alignment.Bottom)
        )
    }
}

@Composable
fun IntSize.getWidthDp(): Dp = LocalDensity.current.run { width.toDp() }

@Composable
fun IntSize.getHeightDp(): Dp = LocalDensity.current.run { height.toDp() }
