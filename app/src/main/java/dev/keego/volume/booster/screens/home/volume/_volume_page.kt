package dev.keego.volume.booster.screens.home.volume

import android.app.Activity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.keego.volume.booster.model.PlaybackCommand
import dev.keego.volume.booster.screens.home.component._volume_button
import dev.keego.volume.booster.screens.home.component._volume_control
import dev.keego.volume.booster.screens.home.component._volume_playback
import dev.keego.volume.booster.screens.home.component._volume_slider
import dev.keego.volume.booster.shared.tag.TagTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun _volume_page(
    modifier: Modifier = Modifier,
    tag: TagTheme = TagTheme.DEFAULT
) {
    val context = LocalContext.current as Activity
    val viewModel = hiltViewModel<VolumeViewModel>()
    val visualizerData by viewModel.visualizerData.collectAsStateWithLifecycle()

    val thirtyLambdaOnClick = remember<(Int) -> Unit> {
        { viewModel.updateBoostValue(context, it) }
    }
    val putPlaybackCommand = remember<(PlaybackCommand) -> Unit> {
        { viewModel.putPlaybackCommand(it) }
    }
    val buttons = listOf(
        VolumeButton(label = "Mute") { thirtyLambdaOnClick.invoke(0) },
        VolumeButton(label = "30%") { thirtyLambdaOnClick.invoke(30) },
        VolumeButton(label = "60%") { thirtyLambdaOnClick.invoke(60) },
        VolumeButton(label = "100%") { thirtyLambdaOnClick.invoke(100) },
        VolumeButton(label = "200%") { thirtyLambdaOnClick.invoke(200) },
        VolumeButton(label = "300%") { thirtyLambdaOnClick.invoke(300) },
        VolumeButton(label = "400%") { thirtyLambdaOnClick.invoke(400) },
        VolumeButton(label = "MAX") { thirtyLambdaOnClick.invoke(500) }
    )
    LaunchedEffect(key1 = true) {
        viewModel.initSettingContentObserver(context)
    }
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        _volume_control(
            modifier = Modifier.fillMaxWidth(),
            visualizerData = visualizerData
        )
        _volume_slider(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
            value = 0.3f,
            isMute = false,
            onSpeakerClick = { },
            onValueChange = {}
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp)
        ) {
            buttons.take(4).forEach { button ->
                _volume_button(
                    modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
                    label = button.label,
                    onClick = button.onClick
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp)
        ) {
            buttons.takeLast(4).forEach { button ->
                _volume_button(
                    modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
                    label = button.label,
                    onClick = button.onClick
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        _volume_playback(
            onPlay = { putPlaybackCommand.invoke(PlaybackCommand.Play) },
            onPause = { putPlaybackCommand.invoke(PlaybackCommand.Pause) },
            onPrevious = { putPlaybackCommand.invoke(PlaybackCommand.Previous) },
            onNext = { putPlaybackCommand.invoke(PlaybackCommand.Next) }
        )
    }
}

data class VolumeButton(
    val label: String,
    val onClick: () -> Unit = {}
)
