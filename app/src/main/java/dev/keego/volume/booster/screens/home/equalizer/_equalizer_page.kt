package dev.keego.volume.booster.screens.home.equalizer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.keego.volume.booster.model.PlaybackCommand
import dev.keego.volume.booster.screens.home.component._equalizer_bass_virtual
import dev.keego.volume.booster.screens.home.component._equalizer_frequencies
import dev.keego.volume.booster.screens.home.component._equalizer_playback
import dev.keego.volume.booster.screens.home.component._equalizer_preset
import dev.keego.volume.booster.shared.tag.TagTheme

@Composable
fun _equalizer_page(
    modifier: Modifier = Modifier,
    tag: TagTheme = TagTheme.DEFAULT
) {
    val viewModel = hiltViewModel<EqualizerViewModel>()
    val visualizerData by viewModel.visualizerData.collectAsStateWithLifecycle()
    val bassStrength by viewModel.bassStrength.collectAsStateWithLifecycle()
    val virtualizerStrength by viewModel.virtualizerStrenth.collectAsStateWithLifecycle()
    val enable by viewModel.enable.collectAsStateWithLifecycle()
    val lambdaPlaybackCommand = remember<(PlaybackCommand) -> Unit> {
        {
            viewModel.putPlaybackCommand(it)
        }
    }
    val lambdaUpdateBoostStrenth = remember<(Int) -> Unit> {
        {
            viewModel.updateBassStrength(it.toShort())
        }
    }
    val lambdaUpdateVirtualizerStrenth = remember<(Int) -> Unit> {
        {
            viewModel.updateVirtualizerStrength(it.toShort())
        }
    }
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        _equalizer_preset(
            modifier = Modifier.padding(16.dp),
            enable = enable
        ) {
            viewModel.toggleEnableEqualizer(it)
        }
        _equalizer_bass_virtual(
            modifier = Modifier,
            enable = enable,
            visualizerData = visualizerData,
            virtualizerValue = virtualizerStrength,
            bassBoostValue = bassStrength,
            maxBassValue = 1000,
            maxVirtualizerValue = 1000,
            onBassBoostValueChange = lambdaUpdateBoostStrenth,
            onVirtualizeValueChange = lambdaUpdateVirtualizerStrenth
        )
        _equalizer_frequencies(
            modifier = Modifier.padding(top = 24.dp).height(180.dp).fillMaxWidth(),
//            frequenciesData = listOf(),
            enable = enable,
            onFrequencyChange = {}
        )
        Spacer(modifier = Modifier.weight(1f))
        _equalizer_playback(
            onContentClick = { lambdaPlaybackCommand.invoke(PlaybackCommand.ContentClick) },
            onPlay = { lambdaPlaybackCommand.invoke(PlaybackCommand.Play) },
            onPause = { lambdaPlaybackCommand.invoke(PlaybackCommand.Pause) },
            onPrevious = { lambdaPlaybackCommand.invoke(PlaybackCommand.Previous) },
            onNext = { lambdaPlaybackCommand.invoke(PlaybackCommand.Next) }
        )
    }
}
