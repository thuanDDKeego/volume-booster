package dev.keego.volume.booster.screens.presets

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.keego.volume.booster.R
import dev.keego.volume.booster.screens.home.equalizer.EqualizerViewModel
import dev.keego.volume.booster.shared.ui._general_top_bar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Destination
fun presets_(
    navigator: DestinationsNavigator,
    viewModel: EqualizerViewModel
) {
    val presets by viewModel.presets.collectAsStateWithLifecycle()
    val currentPreset by viewModel.currentPreset.collectAsStateWithLifecycle()

    val customBackHandler = { navigator.navigateUp() }
    BackHandler {
        customBackHandler.invoke()
    }
    Scaffold(
        topBar = {
            _general_top_bar(
                title = {
                    Text(
                        text = stringResource(id = R.string.presets),
                        style = MaterialTheme.typography.titleMedium.copy(
                            MaterialTheme.colorScheme.onSurface
                        )
                    )
                },
                navigation = {
                    IconButton(onClick = {
                        customBackHandler()
                    }) {
                        Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = "back")
                    }
                }
            )
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                columns = GridCells.Fixed(3),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(items = presets, key = { it.id }) { preset ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable(enabled = true) {
                            viewModel.updateCurrentPreset(preset)
                        }
                    ) {
                        Image(
                            painter = painterResource(id = preset.thumb),
                            contentDescription = "preset thumb",
                            modifier = Modifier
                                .padding(4.dp)
                                .fillMaxWidth()
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(16.dp))
                                .border(
                                    width = if (currentPreset.id == preset.id) (1.5).dp else 0.dp,
                                    color = if (currentPreset.id == preset.id) {
                                        MaterialTheme.colorScheme.secondary
                                    } else {
                                        MaterialTheme.colorScheme.surface
                                    },
                                    shape = RoundedCornerShape(16.dp)
                                ),
                            contentScale = ContentScale.Crop
                        )
                        Text(
                            text = preset.name,
                            style = MaterialTheme.typography.titleSmall.copy(
                                color = MaterialTheme.colorScheme.onBackground
                            ),
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }
        }
    }
}
