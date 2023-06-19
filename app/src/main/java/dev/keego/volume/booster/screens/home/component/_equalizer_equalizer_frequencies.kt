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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun _equalizer_frequencies(
    modifier: Modifier = Modifier,
    numberOfFrequencies: Int = 5,
    frequenciesData: List<Pair<Int, Int>>,
    onFrequencyChange: (Pair<Int, Int>) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier.fillMaxSize()
    ) {
        val defaultFrequencies = listOf(31, 62, 125, 250, 500)
        for (i in 0 until numberOfFrequencies) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                _equalizer_slider(
                    modifier = Modifier.weight(1f),
                    value = 1f,
                    onValueChange = {}
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${if (frequenciesData.size > i) frequenciesData[i].first else defaultFrequencies[i]}Hz",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color.White.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center
                    )
                )
            }
        }
    }
}
