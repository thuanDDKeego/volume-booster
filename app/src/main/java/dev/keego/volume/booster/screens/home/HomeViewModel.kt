package dev.keego.volume.booster.screens.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.keego.volume.booster.section.model.PlaybackCommand
import dev.keego.volume.booster.section.repositories.AddOnFeaturesRepository
import dev.keego.volume.booster.section.repositories.BoostServiceRepository
import dev.keego.volume.booster.section.repositories.NotificationPlaybackRepository
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val notificationPlaybackRepository: NotificationPlaybackRepository,
    private val boostServiceRepository: BoostServiceRepository,
    private val addOnFeaturesRepository: AddOnFeaturesRepository
) :
    ViewModel() {
    val enabledVibration = addOnFeaturesRepository.enabledVibrate
    val enabledEqualizer = boostServiceRepository.enableEqualizer
    val playback = notificationPlaybackRepository.playback
    val playbackColor = notificationPlaybackRepository.color

    fun putPlaybackCommand(command: PlaybackCommand) {
        notificationPlaybackRepository.putCommand(command)
    }

    fun toggleEnabledVibration(value: Boolean) {
        addOnFeaturesRepository.toggleVibration(value)
    }

    fun toggleEnabledEqualizer(value: Boolean) {
        boostServiceRepository.toggleEnableEqualizer(value)
    }
}
