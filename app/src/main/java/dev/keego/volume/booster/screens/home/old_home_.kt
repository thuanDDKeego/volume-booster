package dev.keego.volume.booster.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.keego.volume.booster.section.model.PlaybackCommand
import dev.keego.volume.booster.screens.home.component._circular_progress_indicator
import timber.log.Timber

@Composable
fun home_(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val playback by viewModel.playbackState.collectAsStateWithLifecycle()
    val onPlay = remember { { viewModel.putCommand(PlaybackCommand.Play) } }
    val onPause = remember { { viewModel.putCommand(PlaybackCommand.Pause) } }
    val onNext = remember { { viewModel.putCommand(PlaybackCommand.Next) } }
    val onPrevious = remember { { viewModel.putCommand(PlaybackCommand.Previous) } }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
    ) {
        _volume_boost_section()
        Spacer(modifier = Modifier.height(24.dp))
        _playback_section(
            modifier = Modifier,
            playback = playback,
            onPlay = onPlay,
            onPause = onPause,
            onNext = onNext,
            onPrevious = onPrevious
        )
        Spacer(modifier = Modifier.height(24.dp))
        _equalizer()
    }
}

@Composable
fun _volume_boost_section(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val viewModel: HomeViewModel = hiltViewModel()
    val boostState by viewModel.boostState.collectAsStateWithLifecycle()
//    LaunchedEffect(true) {
//        viewModel.setupBoostService(context)
//    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentSize()
            .padding(vertical = 24.dp, horizontal = 36.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(Color(0xFF81D4FA)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
//        UpdateCustomCircularProgressIndicator(
//            modifier = Modifier
//                .size(250.dp)
//                .background(Color.DarkGray),
//            initialValue = 50,
//            primaryColor = Color(0xFFFF5722),
//            secondaryColor = Color(0xFFAFAFAF),
//            circleRadius = 230f,
//            onPositionChange = { position ->
//                Timber.d("onPositionChange $position")
//                // do something with this position value
//            }
//        )
//        _old_circular_progress_indicator(
//            modifier = Modifier
//                .size(250.dp)
//                .background(Color.DarkGray),
//            initialValue = 50,
//            primaryColor = Color(0xFFFF5722),
//            secondaryColor = Color(0xFFAFAFAF),
//            progressSize = 230f,
//            circleRadius = 230f,
//            onValueChange = { position ->
//                Timber.d("onPositionChange $position")
//                // do something with this position value
//            }
//        )
        Switch(
            checked = boostState.enable,
            onCheckedChange = { viewModel.toggleEnableBoost(context, it) }
        )
        Slider(
            value = boostState.db.toFloat(),
            onValueChange = {
                viewModel.updateBoostValue(it)
            },
            // 0db to 300db
            valueRange = 0f..6000f,
            modifier = Modifier.fillMaxWidth().padding(24.dp)
        )
    }
}

@Composable
fun _playback_section(
    modifier: Modifier = Modifier,
    playback: HomeViewModel.PlayBackState,
    onPlay: () -> Unit,
    onPause: () -> Unit,
    onPrevious: () -> Unit,
    onNext: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentSize()
            .clip(MaterialTheme.shapes.medium)
            .background(Color(0xFFE6EE9C))
            .padding(vertical = 24.dp, horizontal = 36.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = playback.name)
        Spacer(modifier = Modifier.height(12.dp))
        Row {
            IconButton(onClick = { onPrevious() }) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = "",
                    modifier = Modifier.size(24.dp),
                    tint = Color.Black
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            IconButton(onClick = { if (playback.isPlaying) onPause() else onPlay() }) {
                Icon(
                    imageVector = if (playback.isPlaying) Icons.Rounded.Close else Icons.Rounded.PlayArrow,
                    contentDescription = "",
                    modifier = Modifier.size(24.dp),
                    tint = Color.Black
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            IconButton(onClick = { onNext() }) {
                Icon(
                    imageVector = Icons.Rounded.ArrowForward,
                    contentDescription = "",
                    modifier = Modifier.size(24.dp),
                    tint = Color.Black
                )
            }
        }
    }
}

@Composable
fun _equalizer(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val viewModel: HomeViewModel = hiltViewModel()
    val bandLevel by viewModel.frequencyBandLevel.collectAsStateWithLifecycle()
//    LaunchedEffect(true) {
//        viewModel.setupBoostService(context)
//    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentSize()
            .padding(vertical = 24.dp, horizontal = 36.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(Color(0xFF81D4FA)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        bandLevel.forEach {
            Slider(
                value = it.second.toFloat(),
                onValueChange = { value ->
                    viewModel.updateBandValue(it.first, value.toInt())
                },
                // 0db to 300db
                valueRange = -1500f..1500f,
                modifier = Modifier.fillMaxWidth().padding(24.dp)
            )
        }
    }
}

// @Preview
// @Composable
// fun home_preview() {
//    home_()
// }
