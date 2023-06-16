package dev.keego.volume.booster.screens.home.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.keego.volume.booster.R
import dev.keego.volume.booster.shared.tag.TagTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun _volume_slider(
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    value: Float,
    isMute: Boolean,
    tag: TagTheme = TagTheme.DEFAULT,
    onSpeakerClick: (Boolean) -> Unit,
    onValueChange: (Float) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(end = 8.dp)
        ) {
            Icon(
                painter = painterResource(
                    id = if (isMute) R.drawable.ic_mute else R.drawable.ic_speaker
                ),
                contentDescription = "speaker",
                modifier = Modifier
                    .size(20.dp)
                    .clickable {
                        onSpeakerClick(!isMute)
                    },
                tint = Color(0xFF03A9F4)
            )
            Text(
                text = stringResource(id = R.string.system),
                style = MaterialTheme.typography.labelMedium.copy(
                    MaterialTheme.colorScheme.onBackground
                )
            )
        }
        Slider(
            modifier = modifier.fillMaxWidth(),
            value = value,
            valueRange = valueRange,
            onValueChange = onValueChange,
            enabled = false,
            colors = SliderDefaults.colors(
                thumbColor = Color(0xFF3F51B5),
                activeTrackColor = Color(0xFF03A9F4),
                activeTickColor = Color(0xFF03A9F4),
                inactiveTrackColor = Color(0XFF464646)
            )
        ) {
            Icon(
                imageVector = Icons.Filled.AddCircle,
                contentDescription = "Heart Shape",
                modifier = Modifier
                    .offset(y = (2).dp)
                    .size(18.dp),
                tint = Color(0xFF3F51B5)
            )
        }
    }
}
