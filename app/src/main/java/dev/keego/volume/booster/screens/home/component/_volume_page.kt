package dev.keego.volume.booster.screens.home.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.keego.volume.booster.shared.tag.TagTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun _volume_page(
    modifier: Modifier = Modifier,
    tag: TagTheme = TagTheme.DEFAULT,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        _volume_control(
            modifier = Modifier.fillMaxWidth(),
        )
        Slider(
            modifier = Modifier.fillMaxWidth(),
            value = 0.5f,
            valueRange = 0f..1f,
            onValueChange = {
            },
            enabled = false,
            colors = SliderDefaults.colors(
                thumbColor = Color(0xFF3F51B5),
                activeTrackColor = Color(0xFF03A9F4),
                activeTickColor = Color(0xFF03A9F4),
                inactiveTrackColor = Color(0XFF464646),
            ),
        ) {
            Icon(
                imageVector = Icons.Rounded.FavoriteBorder,
                contentDescription = "Heart Shape",
                modifier = Modifier
                    .offset(y = (2).dp)
                    .size(18.dp),
                tint = Color(0xFF3F51B5),
            )
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}
