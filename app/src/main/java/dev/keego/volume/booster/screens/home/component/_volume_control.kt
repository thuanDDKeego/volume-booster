package dev.keego.volume.booster.screens.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import dev.keego.volume.booster.shared.tag.TagTheme
import dev.keego.volume.booster.shared.utils.Dimensions.toDp

@Composable
fun _volume_control(
    modifier: Modifier = Modifier,
    tag: TagTheme = TagTheme.DEFAULT,
) {
    val context = LocalContext.current
    val displayMetrics = context.resources.displayMetrics
    val screenWidth = displayMetrics.widthPixels
    Row(modifier = modifier.wrapContentSize()) {
//  TODO uppdate 2 visualizer bars here
        Spacer(modifier = Modifier.weight(1f))
        _circular_progress_indicator(
            modifier = modifier.background(Color.Black),
            initialValue = 0,
            primaryColor = MaterialTheme.colorScheme.primary,
            secondaryColor = MaterialTheme.colorScheme.secondary,
            progressSize = (screenWidth * 0.5f).toDp(context),
            circleRadius = (screenWidth * 0.45f).toDp(context),
        ) {
            // TODO update volume
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}
