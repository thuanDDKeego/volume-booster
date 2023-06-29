package dev.keego.volume.booster.screens.home.volume

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.canopas.lib.showcase.ShowcaseStyle
import com.canopas.lib.showcase.introShowCaseTarget
import dev.keego.volume.booster.LocalIntroShowCase
import dev.keego.volume.booster.R
import dev.keego.volume.booster.screens.home.component._volume_button
import dev.keego.volume.booster.screens.home.component._volume_control
import dev.keego.volume.booster.screens.home.component._volume_slider
import dev.keego.volume.booster.shared.tag.TagTheme
import kotlin.math.max
import kotlin.math.roundToInt
import timber.log.Timber

@Composable
fun _volume_page(
    modifier: Modifier = Modifier,
    tag: TagTheme = TagTheme.DEFAULT
) {
    val viewModel = hiltViewModel<VolumeViewModel>()
    val visualizerData by viewModel.visualizerData.collectAsStateWithLifecycle()
    val boostValue by viewModel.boostValue.collectAsStateWithLifecycle()

    val introShowCaseState = LocalIntroShowCase.current

    val lambdaUpdateBoostValue = remember<(Int) -> Unit> {
        { viewModel.updateBoostValue(it) }
    }
//    val putPlaybackCommand = remember<(PlaybackCommand) -> Unit> {
//        { viewModel.putPlaybackCommand(it) }
//    }
    val buttons = listOf(
        VolumeButton(label = "Mute") { lambdaUpdateBoostValue.invoke(0) },
        VolumeButton(label = "30%") { lambdaUpdateBoostValue.invoke(30) },
        VolumeButton(label = "60%") { lambdaUpdateBoostValue.invoke(60) },
        VolumeButton(label = "100%") { lambdaUpdateBoostValue.invoke(100) },
        VolumeButton(label = "200%") { lambdaUpdateBoostValue.invoke(200) },
        VolumeButton(label = "300%") { lambdaUpdateBoostValue.invoke(300) },
        VolumeButton(label = "400%") { lambdaUpdateBoostValue.invoke(400) },
        VolumeButton(label = "MAX") { lambdaUpdateBoostValue.invoke(500) }
    )
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        _volume_control(
            modifier = Modifier.fillMaxWidth(),
            visualizerData = visualizerData,
            boostValue = max(0, boostValue - 100)
        ) { value ->
            lambdaUpdateBoostValue.invoke(value + 100)
        }
        _volume_slider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            value = boostValue.toFloat(),
            valueRange = 0f..100f,
            isMute = false,
            onSpeakerClick = {},
            onValueChange = {
                lambdaUpdateBoostValue(it.roundToInt())
            }
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
            buttons.takeLast(4).forEachIndexed { index, button ->
                var modifier = Modifier.weight(1f).padding(horizontal = 4.dp)
                if (index == 0) {
                    Timber.d("introShowCaseState index 3 ")
                    modifier = modifier.introShowCaseTarget(
                        state = introShowCaseState,
                        index = 4,
                        style = ShowcaseStyle.Default.copy(
                            // specify color of background
                            backgroundColor = MaterialTheme.colorScheme.primary,
                            // specify transparency of background
                            backgroundAlpha = 0.98f,
                            // specify color of target circle
                            targetCircleColor = Color.White
                        ),
                        content = {
                            Column {
                                Text(
                                    text = stringResource(id = R.string.quick_select),
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                )
                                Text(
                                    text = stringResource(
                                        id = R.string.quick_adjust_volume
                                    ),
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                )
                            }
                        }
                    )
                }
                _volume_button(
                    modifier = modifier,
                    label = button.label,
                    onClick = button.onClick
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
//        _volume_playback(
//            color = colorPlayback,
//            onContentClick = { putPlaybackCommand.invoke(PlaybackCommand.ContentClick) },
//            onPlay = { putPlaybackCommand.invoke(PlaybackCommand.Play) },
//            onPause = { putPlaybackCommand.invoke(PlaybackCommand.Pause) },
//            onPrevious = { putPlaybackCommand.invoke(PlaybackCommand.Previous) },
//            onNext = { putPlaybackCommand.invoke(PlaybackCommand.Next) }
//        )
    }
}

data class VolumeButton(
    val label: String,
    val onClick: () -> Unit = {}
)
