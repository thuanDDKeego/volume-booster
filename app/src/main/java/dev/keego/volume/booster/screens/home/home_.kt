package dev.keego.volume.booster.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.keego.volume.booster.model.Command

@Composable
fun home_(
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val playback by viewModel.playbackState.collectAsStateWithLifecycle()
    val onPlay = remember { { viewModel.putCommand(Command.Play) } }
    val onPause = remember { { viewModel.putCommand(Command.Pause) } }
    val onNext = remember { { viewModel.putCommand(Command.Next) } }
    val onPrevious = remember { { viewModel.putCommand(Command.Previous) } }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize(),
    ) {
        _playback_section(
            modifier = Modifier,
            playback = playback,
            onPlay = onPlay,
            onPause = onPause,
            onNext = onNext,
            onPrevious = onPrevious,
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
    onNext: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentSize()
            .clip(MaterialTheme.shapes.medium)
            .background(Color(0xFFE6EE9C))
            .padding(vertical = 24.dp, horizontal = 36.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = playback.name)
        Spacer(modifier = Modifier.height(12.dp))
        Row {
            IconButton(onClick = { onPrevious() }) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = "",
                    modifier = Modifier.size(24.dp),
                    tint = Color.Black,
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            IconButton(onClick = { if (playback.isPlaying) onPause() else onPlay() }) {
                Icon(
                    imageVector = if (playback.isPlaying) Icons.Rounded.Close else Icons.Rounded.PlayArrow,
                    contentDescription = "",
                    modifier = Modifier.size(24.dp),
                    tint = Color.Black,
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            IconButton(onClick = { onNext() }) {
                Icon(
                    imageVector = Icons.Rounded.ArrowForward,
                    contentDescription = "",
                    modifier = Modifier.size(24.dp),
                    tint = Color.Black,
                )
            }
        }
    }
}

// @Preview
// @Composable
// fun home_preview() {
//    home_()
// }
