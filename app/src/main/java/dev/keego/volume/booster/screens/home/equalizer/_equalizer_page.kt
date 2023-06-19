package dev.keego.volume.booster.screens.home.equalizer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.keego.volume.booster.model.PlaybackCommand
import dev.keego.volume.booster.screens.home.component._equalizer_bass_virtual
import dev.keego.volume.booster.screens.home.component._equalizer_frequencies
import dev.keego.volume.booster.screens.home.component._equalizer_playback
import dev.keego.volume.booster.shared.tag.TagTheme

@Composable
fun _equalizer_page(
    modifier: Modifier = Modifier,
    tag: TagTheme = TagTheme.DEFAULT
) {
    val viewModel = hiltViewModel<EqualizerViewModel>()
    val lambdaPlaybackCommand = remember<(PlaybackCommand) -> Unit> {
        {
            viewModel.putPlaybackCommand(it)
        }
    }
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        _equalizer_bass_virtual(
            modifier = Modifier,
            visualizerData = 50,
            virtualizeValue = 0.5f,
            bassBoostValue = 0.5f,
            onBassBoostValueChange = {},
            onVirtualizeValueChange = {}
        )
        _equalizer_frequencies(
            modifier = Modifier.padding(top = 24.dp).height(180.dp).fillMaxWidth(),
            frequenciesData = listOf(),
            onFrequencyChange = {}
        )
        Spacer(modifier = Modifier.weight(1f))
        _equalizer_playback(
            onPlay = { lambdaPlaybackCommand.invoke(PlaybackCommand.Play) },
            onPause = { lambdaPlaybackCommand.invoke(PlaybackCommand.Pause) },
            onPrevious = { lambdaPlaybackCommand.invoke(PlaybackCommand.Previous) },
            onNext = { lambdaPlaybackCommand.invoke(PlaybackCommand.Next) }
        )
    }
}
