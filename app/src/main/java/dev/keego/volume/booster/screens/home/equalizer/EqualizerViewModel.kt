package dev.keego.volume.booster.screens.home.equalizer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.keego.volume.booster.R
import dev.keego.volume.booster.section.local.preset.Preset
import dev.keego.volume.booster.section.repositories.BoostServiceRepository
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
    private val presetRepository: PresetRepository
) :
    ViewModel() {

    val enable = volumeBoostRepository.enableEqualizer
    val visualizerData = volumeBoostRepository.visualizerArray
    val bandLevel = volumeBoostRepository.bandValue
    val bassStrength = volumeBoostRepository.bassStrength
    val virtualizerStrenth = volumeBoostRepository.virtualizerStrength

    private val _presets = MutableStateFlow<List<Preset>>(listOf())
    val presets = _presets.asStateFlow()

    private val _currentPreset = MutableStateFlow(customPreset)
    val currentPreset = _currentPreset.asStateFlow()

    init {
        viewModelScope.launch {
            presetRepository.getAll().collectLatest {
                _presets.value = it.sortedBy { -it.timeCreated }
            }
        }
    }

    fun toggleEnableEqualizer(value: Boolean) {
        volumeBoostRepository.toggleEnableEqualizer(value)
    }

    fun updateBandValue(frequency: Int, value: Int) {
        Timber.d("updateBandValue viewModel join $frequency --- $value")
        volumeBoostRepository.updateBandValue(frequency, value)
        customPreset = customPreset.copy(
            bandLevels = bandLevel.value.map { i ->
                i.second
            }
        )
        _currentPreset.value = customPreset
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

    fun updateFrequenciesFollowPreset() {
        bandLevel.value.forEachIndexed { index, fre ->
            volumeBoostRepository.updateBandValue(fre.first, currentPreset.value.bandLevels[index])
        }
    }

    fun savePreset(name: String, onError: (message: String) -> Unit, onSuccess: () -> Unit) {
        if (_presets.value.any {
                it.name.trim().lowercase() == name.trim().lowercase()
            }
        ) {
            onError("Name already exists, try another")
            return
        }
        viewModelScope.launch {
            presetRepository.insert(
                _currentPreset.value.copy(
                    id = 0,
                    name = name,
                    timeCreated = System.currentTimeMillis()
                )
            )
            onSuccess()
        }
    }

    fun getSuggestNameNewPreset(): String {
        var counter = 1
        var newCustomPresetName = "Custom $counter"
        while (_presets.value.any {
                it.name.trim().lowercase() == newCustomPresetName.trim().lowercase()
            }
        ) {
            newCustomPresetName = "Custom ${++counter}"
        }
        return newCustomPresetName
    }

    fun revertPreset() {
        bandLevel.value.forEach {
            updateBandValue(it.first, 0)
        }
    }
}

var customPreset = Preset(
    id = -1,
    name = "Custom",
    isDefault = false,
    thumb = R.drawable.ic_vinyl,
    bandLevels = listOf(0, 0, 0, 0, 0)
)
