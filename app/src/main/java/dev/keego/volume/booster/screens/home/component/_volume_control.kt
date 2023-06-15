package dev.keego.volume.booster.screens.home.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
    Row(modifier = modifier) {
//  TODO uppdate 2 visualizer bars here
        Spacer(modifier = Modifier.weight(1f))
        _circular_progress_indicator(
            modifier = modifier.width((screenWidth / 2f).toDp(context).dp),
            initialValue = 0,
            primaryColor = MaterialTheme.colorScheme.primary,
            secondaryColor = MaterialTheme.colorScheme.secondary,
            circleRadius = (screenWidth / 2f).toDp(context) - 20f.toDp(context),
        ) {
            // TODO update volume
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}
