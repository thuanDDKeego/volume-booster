package dev.keego.volume.booster.screens.home.volume

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.keego.volume.booster.section.repositories.AddOnFeaturesRepository
import dev.keego.volume.booster.section.repositories.BoostServiceRepository
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class VolumeViewModel @Inject constructor(
    private val volumeBoostRepository: BoostServiceRepository,
    private val addOnFeaturesRepository: AddOnFeaturesRepository
) :
    ViewModel() {

    val visualizerData = volumeBoostRepository.visualizerArray
    val boostValue = volumeBoostRepository.db

    // range from 0 .. 400
    fun updateBoostValue(value: Int) {
        volumeBoostRepository.updateBoostValue(value)
        /*when value > 100, we start boost,
        and we will vibrate device
         */
//        val vibrateStrength = ((value.toFloat() - 100) / 300f).coerceIn(0f, 1f)
        if (value in 101..399) {
            /* total angle is 240 so we vibrate stronger when value in range belo
            * around 40, 80, 120, 160, 200, 240 (around +- 5 degree)
            * */
            var boostStronger = false
            // 6 ranges
            listOf(1f, 2f, 3f, 4f, 5f, 6f).map {
                val multiplier = it * 40f / 240f
                // cause value range in 100 to 400
                val valueConverted = (value * 300f + 100f).roundToInt()
                val fiveDegreeToValue = (10f / 240f * 300f).roundToInt()
                IntRange(valueConverted - fiveDegreeToValue, valueConverted + fiveDegreeToValue)
            }.any { range ->
                range.contains(value)
            }.let {
                boostStronger = it
            }
            val vibrateStrength = if (boostStronger) 1f else 0.6f
            addOnFeaturesRepository.vibrate(vibrateStrength, 150L)
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}
