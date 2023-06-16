package dev.keego.volume.booster.screens.home.equalizer

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.keego.volume.booster.repositories.BoostServiceRepository
import javax.inject.Inject

@HiltViewModel
class EqualizerViewModel @Inject constructor(
    private val volumeBoostRepository: BoostServiceRepository,
) :
    ViewModel() {}
