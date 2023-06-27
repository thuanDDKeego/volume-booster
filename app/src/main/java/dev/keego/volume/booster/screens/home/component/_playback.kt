package dev.keego.volume.booster.screens.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import dev.keego.volume.booster.R
import dev.keego.volume.booster.section.repositories.PlayBackState
import dev.keego.volume.booster.screens.home.equalizer.EqualizerViewModel
import dev.keego.volume.booster.screens.home.volume.VolumeViewModel

@Composable
fun _volume_playback(
    modifier: Modifier = Modifier,
    onContentClick: () -> Unit,
    onPlay: () -> Unit,
    onPause: () -> Unit,
    onPrevious: () -> Unit,
    onNext: () -> Unit
) {
    val viewModel = hiltViewModel<VolumeViewModel>()
    val playback by viewModel.playbackState.collectAsStateWithLifecycle()
    _playback(
        modifier = modifier,
        playback = playback,
        onContentClick = onContentClick,
        onPlay = onPlay,
        onPause = onPause,
        onPrevious = onPrevious,
        onNext = onNext
    )
}

@Composable
fun _equalizer_playback(
    modifier: Modifier = Modifier,
    onContentClick: () -> Unit,
    onPlay: () -> Unit,
    onPause: () -> Unit,
    onPrevious: () -> Unit,
    onNext: () -> Unit
) {
    val viewModel = hiltViewModel<EqualizerViewModel>()
    val playback by viewModel.playbackState.collectAsStateWithLifecycle()
    _playback(
        modifier = modifier,
        playback = playback,
        onContentClick = onContentClick,
        onPlay = onPlay,
        onPause = onPause,
        onPrevious = onPrevious,
        onNext = onNext
    )
}

@Composable
private fun _playback(
    modifier: Modifier = Modifier,
    playback: PlayBackState,
    onContentClick: () -> Unit,
    onPlay: () -> Unit,
    onPause: () -> Unit,
    onPrevious: () -> Unit,
    onNext: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
            .clickable(enabled = false) {
                onContentClick()
            }
            .background(Color(playback.color))
            .padding(vertical = 12.dp, horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = playback.thumb?.let {
                rememberAsyncImagePainter(model = it)
            } ?: painterResource(
                id = R.drawable.ic_vinyl
            ),
            modifier = Modifier
                .size(42.dp)
                .clip(shape = MaterialTheme.shapes.extraSmall)
                .background(Color(0xFF81C784)),
            contentDescription = "song thumb"
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = playback.song,
                style = MaterialTheme.typography.headlineSmall.copy(fontSize = 13.sp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = playback.artist,
                style = MaterialTheme.typography.labelMedium.copy(fontSize = 13.sp),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        IconButton(onClick = { onPrevious() }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_previous),
                contentDescription = "",
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(
            onClick = if (playback.isPlaying) onPause else onPlay
        ) {
            Icon(
                painter = painterResource(
                    id = if (playback.isPlaying) R.drawable.ic_pause else R.drawable.ic_play
                ),
                contentDescription = "",
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(onClick = onNext) {
            Icon(
                painter = painterResource(id = R.drawable.ic_next_song),
                contentDescription = "",
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
