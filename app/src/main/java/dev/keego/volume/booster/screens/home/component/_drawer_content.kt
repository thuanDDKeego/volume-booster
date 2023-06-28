package dev.keego.volume.booster.screens.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.keego.volume.booster.R

@Composable
fun _drawer_content(
    modifier: Modifier = Modifier,
    vibrationValue: Boolean,
    equalizerValue: Boolean,
    onVibrationValueChange: (Boolean) -> Unit,
    onEqualizerValueChange: (Boolean) -> Unit,
    onLanguageClick: () -> Unit
) {
    Column(horizontalAlignment = Alignment.Start, modifier = modifier) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth()
                .height(200.dp)
                .background(
                    MaterialTheme.colorScheme.primary
                )
        ) {
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    }
    _drawer_item(
        modifier = Modifier.padding(horizontal = 8.dp),
        icon = {
            Image(
                imageVector = Icons.Default.Phone,
                contentDescription = "vibration icon",
                modifier = Modifier.size(24.dp)
            )
        },
        title = stringResource(id = R.string.vibration_setting),
        tailContent = {
            Switch(
                checked = vibrationValue,
                onCheckedChange = onVibrationValueChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.primary,
                    checkedTrackColor = MaterialTheme.colorScheme.surface,
                    uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                    uncheckedTrackColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    )
    _drawer_item(
        modifier = Modifier.padding(horizontal = 8.dp),
        icon = {
            Image(
                imageVector = Icons.Default.Edit,
                contentDescription = "equalizer setting",
                modifier = Modifier.size(24.dp)
            )
        },
        title = stringResource(id = R.string.equalizer_setting),
        tailContent = {
            Switch(
                checked = equalizerValue,
                onCheckedChange = onEqualizerValueChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.primary,
                    checkedTrackColor = MaterialTheme.colorScheme.surface,
                    uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                    uncheckedTrackColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    )
    _drawer_item(
        modifier = Modifier.padding(horizontal = 8.dp),
        icon = {
            Image(
                imageVector = Icons.Default.Search,
                contentDescription = "Languages",
                modifier = Modifier.size(24.dp)
            )
        },
        title = stringResource(id = R.string.languages)
    ) {
        onLanguageClick()
    }
}

@Composable
fun _drawer_item(
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit,
    title: String,
    tailContent: (@Composable () -> Unit)? = null,
    onItemClick: (() -> Unit)? = null
) {
    Row(
        modifier = modifier.fillMaxWidth()
            .clickable(enabled = onItemClick != null) { onItemClick?.invoke() }
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon()
        Text(
            text = title,
            modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
            style = MaterialTheme.typography.titleSmall.copy(
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Start
            )
        )
        tailContent?.invoke()
    }
}
