package dev.keego.volume.booster.screens.home.equalizer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.keego.volume.booster.screens.home.component._bar_equalizer
import dev.keego.volume.booster.shared.tag.TagTheme
import kotlin.random.Random

@Composable
fun _equalizer_page(
    modifier: Modifier = Modifier,
    tag: TagTheme = TagTheme.DEFAULT,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "Visualizer")
        Spacer(modifier = Modifier.height(24.dp))
    }
}
