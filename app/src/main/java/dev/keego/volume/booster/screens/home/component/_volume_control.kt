package dev.keego.volume.booster.screens.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import dev.keego.volume.booster.shared.tag.TagTheme
import dev.keego.volume.booster.shared.utils.Dimensions.toDp

@Composable
fun _volume_control(
    modifier: Modifier = Modifier,
    visualizerData: Int,
    // this value from 100 to 400
    boostValue: Int = 0,
    /* value from 100 to 400
    * but this component, value must be started from 0
    * so max value is 300
    **/
    maxBoostValue: Int = 300,
    tag: TagTheme = TagTheme.DEFAULT,
    onValueChange: (Int) -> Unit
) {
    val context = LocalContext.current
    val displayMetrics = context.resources.displayMetrics
    val screenWidth = displayMetrics.widthPixels
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp)
    ) {
        _bar_visualizer(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .width(28.dp)
                .height(220.dp)
                .background(Color(0x40000000)),
            resolution = 1,
            visualizationData = visualizerData
        )
        _circular_progress_indicator(
            modifier = modifier.align(Alignment.Center).wrapContentSize()
                .background(Color.Black),
            value = boostValue,
            maxValue = maxBoostValue,
            primaryColor = MaterialTheme.colorScheme.primary,
            secondaryColor = MaterialTheme.colorScheme.secondary,
            progressSize = (screenWidth * 0.5f).toDp(context),
            circleRadius = (screenWidth * 0.45f).toDp(context),
            onValueChange = onValueChange
        )
        _bar_visualizer(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .width(28.dp)
                .height(220.dp)
                .background(Color(0x40000000)),
            resolution = 1,
            visualizationData = visualizerData
        )
    }
}

fun generateZeroArray(n: Int) = IntArray(n) { 0 }
