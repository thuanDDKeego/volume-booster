package dev.keego.volume.booster.screens.home.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.keego.volume.booster.shared.tag.TagTheme

@Composable
fun _volume_page(
    modifier: Modifier = Modifier,
    tag: TagTheme = TagTheme.DEFAULT,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        _volume_control(
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
