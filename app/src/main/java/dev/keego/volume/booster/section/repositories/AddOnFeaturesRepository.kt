package dev.keego.volume.booster.section.repositories

import android.content.Context
import dev.keego.volume.booster.shared.utils.Vibration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AddOnFeaturesRepository(private val context: Context) {
    private val _enabledVibrate = MutableStateFlow(true)
    val enabledVibrate = _enabledVibrate.asStateFlow()

    fun toggleVibration(enabled: Boolean) {
        _enabledVibrate.value = enabled
    }

    fun vibrate(strength: Float, duration: Long = 200L) {
        if (_enabledVibrate.value) {
            Vibration.vibrate(context, strength, duration)
        }
    }
}
