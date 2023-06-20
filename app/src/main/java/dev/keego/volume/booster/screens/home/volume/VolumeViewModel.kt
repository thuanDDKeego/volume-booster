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

    val playbackState = notificationblayBackRepository.playback
    val visualizerData = volumeBoostRepository.visualizerArray
    val boostValue =  volumeBoostRepository.db

    // range from 0 .. 400
    fun updateBoostValue(value: Int) {
        volumeBoostRepository.updateBoostValue(value)
    }

    fun putPlaybackCommand(command: PlaybackCommand) {
        notificationblayBackRepository.putCommand(command)
    }

    override fun onCleared() {
        super.onCleared()
    }
}
