package dev.keego.volume.booster.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.keego.volume.booster.model.Command
import dev.keego.volume.booster.repositories.NotificationPlaybackRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val notificationPlaybackRepository: NotificationPlaybackRepository,
) :
    ViewModel() {
    data class PlayBackState(
        val name: String = "No current playing",
        val isPlaying: Boolean = false,
    )

    private val _playbackState = MutableStateFlow(PlayBackState())
    val playbackState = _playbackState.asStateFlow()

    init {
        viewModelScope.launch {
            notificationPlaybackRepository.apply {
                launch {
                    playback.collectLatest {
                        _playbackState.value = _playbackState.value.copy(
                            name = it.name,
                            isPlaying = it.isPlaying,
                        )
                    }
                }
            }
        }
    }

    fun putCommand(command: Command) {
        notificationPlaybackRepository.putCommand(command)
    }
}
