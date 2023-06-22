package dev.keego.volume.booster.screens.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import dev.keego.volume.booster.R

@Composable
fun _equalizer_preset(
    modifier: Modifier = Modifier,
    enable: Boolean = true,
    onToggleEnable: (Boolean) -> Unit
) {
    Row(modifier = modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clip(RoundedCornerShape(12.dp))
                .border(2.dp, MaterialTheme.colorScheme.onBackground, RoundedCornerShape(12.dp))
                .clickable {}
        ) {
            Text(
                text = "Custom",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
            Divider(
                modifier = Modifier
                    .padding(horizontal = 6.dp)
                    .width(2.dp)
                    .heightIn(min = 36.dp)
                    .background(MaterialTheme.colorScheme.onSurface)
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_play),
                contentDescription = "",
                modifier = Modifier.size(12.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(6.dp))
        }
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.clip(RoundedCornerShape(12.dp))
                .clickable { }
                .padding(8.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = "",
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.clip(RoundedCornerShape(12.dp))
                .clickable { }
                .padding(8.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = "",
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.clip(CircleShape)
                .clickable {
                    onToggleEnable(!enable)
                }
                .rotate(30f)
                .background(
                    Brush.horizontalGradient(
                        if (enable) {
                            listOf(
                                Color(0xFF03A9F4),
                                Color(0xFFFFC107),
                                Color(0xFFFF4B13)
                            )
                        } else {
                            listOf(Color(0xFF5E5E5E), Color(0xFF5E5E5E))
                        }
                    )
                )
                .rotate(30f)
                .padding(2.dp)
                .clip(CircleShape)
                .rotate(-30f)
                .background(MaterialTheme.colorScheme.surface)
                .padding(4.dp)

        ) {
            Icon(
                imageVector = Icons.Rounded.Notifications,
                contentDescription = "",
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
