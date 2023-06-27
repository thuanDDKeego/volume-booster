package dev.keego.volume.booster.screens.home.equalizer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.keego.volume.booster.section.local.preset.Preset
import dev.keego.volume.booster.section.model.PlaybackCommand
import dev.keego.volume.booster.section.repositories.BoostServiceRepository
import dev.keego.volume.booster.section.repositories.NotificationPlaybackRepository
import dev.keego.volume.booster.section.repositories.PresetRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class EqualizerViewModel @Inject constructor(
    private val volumeBoostRepository: BoostServiceRepository,
    private val notificationblayBackRepository: NotificationPlaybackRepository,
    private val presetRepository: PresetRepository
) :
    ViewModel() {

    val playbackState = notificationblayBackRepository.playback
    val enable = volumeBoostRepository.enableEqualizer
    val visualizerData = volumeBoostRepository.visualizerArray
    val bandLevel = volumeBoostRepository.bandValue
    val bassStrength = volumeBoostRepository.bassStrength
    val virtualizerStrenth = volumeBoostRepository.virtualizerStrength

    private val _presets = MutableStateFlow<List<Preset>>(listOf())
    val presets = _presets.asStateFlow()

    private val _currentPreset = MutableStateFlow<Preset?>(null)
    val currentPreset = _currentPreset.asStateFlow()

    init {
        viewModelScope.launch {
            presetRepository.getAll().collectLatest {
                _presets.value = it
            }
        }
    }

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

    fun insertPreset(preset: Preset) {
        viewModelScope.launch {
            presetRepository.insert(preset)
        }
    }

    fun deletePreset(preset: Preset) {
        viewModelScope.launch {
            presetRepository.delete(preset)
        }
    }

    fun updateCurrentPreset(preset: Preset) {
        _currentPreset.value = preset
    }
}
