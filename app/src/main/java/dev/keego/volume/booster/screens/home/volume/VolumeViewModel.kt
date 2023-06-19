package dev.keego.volume.booster.screens.home.volume

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.keego.volume.booster.model.PlaybackCommand
import dev.keego.volume.booster.repositories.BoostServiceRepository
import dev.keego.volume.booster.repositories.NotificationPlaybackRepository
import javax.inject.Inject

@HiltViewModel
class VolumeViewModel @Inject constructor(
    private val volumeBoostRepository: BoostServiceRepository,
    private val notificationblayBackRepository: NotificationPlaybackRepository
) :
    ViewModel() {

    val visualizerData = volumeBoostRepository.visualizerArray
    val playbackState = notificationblayBackRepository.playback

    fun updateBoostValue(value: Int) {
    }

    fun putPlaybackCommand(command: PlaybackCommand) {
        notificationblayBackRepository.putCommand(command)
    }
}
