package dev.keego.volume.booster.screens.home.equalizer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.common.collect.ImmutableList
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.keego.volume.booster.screens.destinations.presets_Destination
import dev.keego.volume.booster.screens.home.component._equalizer_bass_virtual
import dev.keego.volume.booster.screens.home.component._equalizer_frequencies
import dev.keego.volume.booster.screens.home.component._equalizer_playback
import dev.keego.volume.booster.screens.home.component._equalizer_preset
import dev.keego.volume.booster.section.model.PlaybackCommand
import dev.keego.volume.booster.shared.tag.TagTheme

@Composable
fun _equalizer_page(
    modifier: Modifier = Modifier,
    navigator: DestinationsNavigator,
    viewModel: EqualizerViewModel,
    tag: TagTheme = TagTheme.DEFAULT
) {
    val visualizerData by viewModel.visualizerData.collectAsStateWithLifecycle()
    val bassStrength by viewModel.bassStrength.collectAsStateWithLifecycle()
    val virtualizerStrength by viewModel.virtualizerStrenth.collectAsStateWithLifecycle()
    val frequenciesData by viewModel.bandLevel.collectAsStateWithLifecycle()
    val preset by viewModel.currentPreset.collectAsStateWithLifecycle()
    val enable by viewModel.enable.collectAsStateWithLifecycle()

    val lambdaEnableEqualizer = remember<(Boolean) -> Unit> {
        {
            viewModel.toggleEnableEqualizer(it)
        }
    }
    val lambdaSavePreset = remember<() -> Unit> {
        {
            viewModel.savePreset()
        }
    }
    val lambdaRevertPreset = remember<() -> Unit> {
        {
            viewModel.revertPreset()
        }
    }
    val lambdaPlaybackCommand = remember<(PlaybackCommand) -> Unit> {
        {
            viewModel.putPlaybackCommand(it)
        }
    }
    val lambdaUpdateBoostStrength = remember<(Int) -> Unit> {
        {
            viewModel.updateBassStrength(it.toShort())
        }
    }
    val lambdaUpdateVirtualizerStrength = remember<(Int) -> Unit> {
        {
            viewModel.updateVirtualizerStrength(it.toShort())
        }
    }
    val lambdaUpdateBandValue = remember<(Pair<Int, Int>) -> Unit> {
        {
            viewModel.updateBandValue(it.first, it.second)
        }
    }

    LaunchedEffect(preset) {
        if (preset.name != customPreset.name) {
            viewModel.updateFrequenciesFollowPreset()
        }
    }
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        _equalizer_preset(
            modifier = Modifier.padding(16.dp),
            enable = enable,
            preset = preset,
            onPresetClick = {
                navigator.navigate(presets_Destination)
            },
            onSaveClick = lambdaSavePreset,
            onRevertClick = lambdaRevertPreset,
            onToggleEnable = lambdaEnableEqualizer
        )
        _equalizer_bass_virtual(
            modifier = Modifier,
            enable = enable,
            visualizerData = visualizerData,
            virtualizerValue = virtualizerStrength,
            bassBoostValue = bassStrength,
            maxBassValue = 1000,
            maxVirtualizerValue = 1000,
            onBassBoostValueChange = lambdaUpdateBoostStrength,
            onVirtualizeValueChange = lambdaUpdateVirtualizerStrength
        )
        _equalizer_frequencies(
            modifier = Modifier.padding(top = 24.dp).height(180.dp).fillMaxWidth(),
            frequenciesData = ImmutableList.copyOf(frequenciesData),
            preset = preset,
            enable = enable,
            onFrequencyChange = lambdaUpdateBandValue
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
