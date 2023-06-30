package dev.keego.volume.booster.screens.home.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.common.collect.ImmutableList
import dev.keego.volume.booster.section.local.preset.Preset

@Composable
fun _equalizer_frequencies(
    modifier: Modifier = Modifier,
    enable: Boolean,
    frequenciesData: ImmutableList<Pair<Int, Int>>,
    preset: Preset,
    numberOfFrequencies: Int = 5,
    onFrequencyChange: (Pair<Int, Int>) -> Unit
) {
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
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 140.dp, max = 160.dp)
            ) {
                val frequencyAnimateValue by animateFloatAsState(
                    targetValue = frequencyValue.toFloat()
                )
                _equalizer_slider(
                    modifier = Modifier.weight(1f),
                    enabled = enable,
                    value = frequencyAnimateValue,
                    valueRange = -1500f..1500f,
                    onValueChange = { value ->
                        if (frequenciesData.size > i) {
                            onFrequencyChange(Pair(frequenciesData[i].first, value.toInt()))
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
