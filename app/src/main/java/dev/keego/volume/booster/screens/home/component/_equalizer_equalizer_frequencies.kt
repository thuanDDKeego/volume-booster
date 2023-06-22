package dev.keego.volume.booster.screens.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.keego.volume.booster.screens.home.equalizer.EqualizerViewModel

@Composable
fun _equalizer_frequencies(
    modifier: Modifier = Modifier,
    enable: Boolean,
    numberOfFrequencies: Int = 5,
    onFrequencyChange: (Pair<Int, Int>) -> Unit
) {
    val viewModel = hiltViewModel<EqualizerViewModel>()
    val frequenciesData by viewModel.bandLevel.collectAsStateWithLifecycle()
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier.fillMaxSize()
    ) {
        val defaultFrequencies = listOf(60000, 230000, 910000, 3600000, 14000000)
        for (i in 0 until numberOfFrequencies) {
            var frequencyValue: Int = 0
            if (frequenciesData.size > i) {
                frequencyValue = frequenciesData[i].second
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                _equalizer_slider(
                    modifier = Modifier.weight(1f),
                    enabled = enable,
                    value = frequencyValue.toFloat(),
                    valueRange = -1500f..1500f,
                    onValueChange = {value ->
                        if (frequenciesData.size > i) {
                            viewModel.updateBandValue(
                                frequenciesData[i].first,
                                value.toInt()
                            )
                        }
                    }
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (frequenciesData.size > i) {
                        frequenciesData[i].first.displayFrequencyName()
                    } else {
                        defaultFrequencies[i].displayFrequencyName()
                    },
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color.White.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center
                    )
                )
            }
        }
    }
}

fun Int.displayFrequencyName(): String {
    if (this < 1000) {
        return "${this}Hz"
    } else if (this < 1000000) {
        return "${this / 1000}Hz"
    } else {
        return "${this / 1000000}kHz"
    }
}
