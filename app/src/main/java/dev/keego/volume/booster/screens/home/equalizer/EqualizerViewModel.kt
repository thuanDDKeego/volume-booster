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

    val visualizerData = volumeBoostRepository.visualizerArray
    val playbackState = notificationblayBackRepository.playback
    val bandLevel = volumeBoostRepository.bandValue

    fun updateBoostValue(value: Int) {
    }

    fun putPlaybackCommand(command: PlaybackCommand) {
        notificationblayBackRepository.putCommand(command)
    }

    fun updateBandValue(frequency: Int, value: Int) {
        Timber.d("updateBandValue viewModel join $frequency --- $value")
        volumeBoostRepository.updateBandValue(frequency, value)
    }
}
