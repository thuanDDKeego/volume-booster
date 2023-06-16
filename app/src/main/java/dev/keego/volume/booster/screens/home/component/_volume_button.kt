package dev.keego.volume.booster.screens.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun _volume_button(
    modifier: Modifier = Modifier,
    label: String,
    onClick: () -> Unit
) {
    Box(contentAlignment = Alignment.Center,
        modifier = modifier.clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable { onClick() }
            .padding(4.dp)
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium)
    }
}
