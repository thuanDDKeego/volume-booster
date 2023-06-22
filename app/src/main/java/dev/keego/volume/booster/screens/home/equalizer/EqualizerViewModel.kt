package dev.keego.volume.booster.screens.home.equalizer

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.keego.volume.booster.model.PlaybackCommand
import dev.keego.volume.booster.repositories.BoostServiceRepository
import dev.keego.volume.booster.repositories.NotificationPlaybackRepository
import javax.inject.Inject
import timber.log.Timber

@HiltViewModel
class EqualizerViewModel @Inject constructor(
    private val volumeBoostRepository: BoostServiceRepository,
    private val notificationblayBackRepository: NotificationPlaybackRepository
) :
    ViewModel() {

    val playbackState = notificationblayBackRepository.playback
    val enable = volumeBoostRepository.enableEqualizer
    val visualizerData = volumeBoostRepository.visualizerArray
    val bandLevel = volumeBoostRepository.bandValue
    val bassStrength = volumeBoostRepository.bassStrength
    val virtualizerStrenth = volumeBoostRepository.virtualizerStrength

    fun toggleEnableEqualizer(value: Boolean) {
        volumeBoostRepository.toggleEnableEqualizer(value)
    }

    fun putPlaybackCommand(command: PlaybackCommand) {
        notificationblayBackRepository.putCommand(command)
    }

    fun updateBandValue(frequency: Int, value: Int) {
        Timber.d("updateBandValue viewModel join $frequency --- $value")
        volumeBoostRepository.updateBandValue(frequency, value)
    }

    fun updateBassStrength(value: Short) {
        volumeBoostRepository.updateBassStrength(value)
    }

    fun updateVirtualizerStrength(value: Short) {
        volumeBoostRepository.updateVirtualizerStrength(value)
    }
}
