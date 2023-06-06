package dev.keego.volume.booster.repositories

import android.app.Notification
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

    private val _command = MutableSharedFlow<Command?>()
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

    fun putCommand(command: Command){
        CoroutineScope(Dispatchers.IO).launch {
            _command.emit(command)
        }
    }
}

sealed class Command() {
    object Play: Command()
    object Pause: Command()
    object Previous: Command()
    object Next: Command()
}
