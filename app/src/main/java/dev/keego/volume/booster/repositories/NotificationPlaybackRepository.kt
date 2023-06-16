package dev.keego.volume.booster.repositories

import android.app.Notification
import android.graphics.Bitmap
import dev.keego.volume.booster.model.PlaybackCommand
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NotificationPlaybackRepository {
    private val _playPauseAction = MutableStateFlow<Notification.Action?>(null)
    val playPauseAction = _playPauseAction.asStateFlow()

    private val _previousAction = MutableStateFlow<Notification.Action?>(null)
    val previousAction = _previousAction.asStateFlow()

    private val _nextAction = MutableStateFlow<Notification.Action?>(null)
    val nextAction = _nextAction.asStateFlow()

    private val _playback = MutableStateFlow<PlayBackState>(PlayBackState())
    val playback = _playback.asStateFlow()

    private val _command = MutableSharedFlow<PlaybackCommand?>()
    val command = _command.asSharedFlow()

    fun updatePlayPauseAction(action: Notification.Action?) {
        _playPauseAction.value = action
    }

    fun updatePreviousAction(action: Notification.Action?) {
        _nextAction.value = action
    }

    fun updateNextAction(action: Notification.Action?) {
        _nextAction.value = action
    }

    fun putCommand(command: PlaybackCommand) {
        CoroutineScope(Dispatchers.IO).launch {
            _command.emit(command)
        }
    }

    fun updatePlayBack(playback: PlayBackState) {
        _playback.value = playback
    }

    fun removePlayBack() {
        _playback.value = PlayBackState()
    }
}

data class PlayBackState(
    val song: String = "Name song",
    val artist: String = "",
    val thumb: Bitmap? = null,
    val color: Int = 0xFF353535.toInt(),
    val isPlaying: Boolean = false
)
