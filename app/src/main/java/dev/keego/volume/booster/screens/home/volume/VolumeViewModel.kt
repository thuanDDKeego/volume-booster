package dev.keego.volume.booster.screens.home.volume

import android.content.Context
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.keego.volume.booster.section.model.PlaybackCommand
import dev.keego.volume.booster.section.repositories.BoostServiceRepository
import dev.keego.volume.booster.section.repositories.NotificationPlaybackRepository
import dev.keego.volume.booster.shared.utils.Vibration
import javax.inject.Inject

@HiltViewModel
class VolumeViewModel @Inject constructor(
    private val volumeBoostRepository: BoostServiceRepository,
    private val notificationplayBackRepository: NotificationPlaybackRepository
) :
    ViewModel() {

    val playbackState = notificationplayBackRepository.playback
    val visualizerData = volumeBoostRepository.visualizerArray
    val boostValue = volumeBoostRepository.db
    val color = notificationplayBackRepository.color

    // range from 0 .. 400
    fun updateBoostValue(context: Context, value: Int) {
        volumeBoostRepository.updateBoostValue(value)
        /*when value > 100, we start boost,
        and we will vibrate device
         */
        val vibrateStrength = ((value.toFloat() - 100) / 300f).coerceIn(0f, 1f)
        Vibration.vibrate(context, strength = vibrateStrength)
    }

    fun putPlaybackCommand(command: PlaybackCommand) {
        notificationplayBackRepository.putCommand(command)
    }

    override fun onCleared() {
        super.onCleared()
    }
}
