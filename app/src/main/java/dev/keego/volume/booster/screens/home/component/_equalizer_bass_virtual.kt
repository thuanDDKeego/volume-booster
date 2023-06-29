package dev.keego.volume.booster.screens.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.keego.volume.booster.R
import dev.keego.volume.booster.shared.ui.AutoSizeText
import dev.keego.volume.booster.shared.utils.Dimensions.toDp
import dev.keego.volume.booster.shared.utils.Dimensions.toPx

@Composable
fun _equalizer_bass_virtual(
    modifier: Modifier = Modifier,
    enable: Boolean,
    visualizerData: Int,
    maxBassValue: Short = 1000,
    maxVirtualizerValue: Short = 1000,
    bassBoostValue: Short,
    virtualizerValue: Short,
    onBassBoostValueChange: (Int) -> Unit,
    onVirtualizeValueChange: (Int) -> Unit
) {
    val context = LocalContext.current
    val displayMetrics = context.resources.displayMetrics
    val screenWidth = displayMetrics.widthPixels
    /* screen width trừ đi padding 2 cạnh, độ rộng của 2 thanh visualize,padding giữa 2 progress(36), padding giữa progress
    * và thanh visualize(12*2)
    * sau đó chia 2 cho mỗi cái
    * */
    val circularProgressSize =
        (
            (
                screenWidth - 24f.toPx(context) - (28f * 2).toPx(context) - 36f.toPx(
                    context
                ) - 24f.toPx(
                    context
                )
                )
            ) / 2f
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth().padding(12.dp)
    ) {
        //
        _bar_visualizer(
            modifier = Modifier
                .width(28.dp)
                .height(180.dp)
                .background(Color(0x40000000)),
            resolution = 1,
            visualizationData = visualizerData
        )
        Spacer(modifier = Modifier.weight(1f))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(circularProgressSize.toDp(context).dp)
        ) {
            _circular_progress_indicator(
                modifier = modifier.wrapContentSize(),
                enable = enable,
                value = bassBoostValue.toInt(),
                maxValue = maxBassValue.toInt(),
                primaryColor = MaterialTheme.colorScheme.primary,
                secondaryColor = MaterialTheme.colorScheme.secondary,
                progressSize = circularProgressSize.toDp(context),
                circleRadius = circularProgressSize * 0.98f.toDp(context),
                onValueChange = onBassBoostValueChange
            )
            Spacer(modifier = Modifier.height(4.dp))
            AutoSizeText(
                text = stringResource(id = R.string.bass_boost),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.titleSmall.copy(
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
        }

        Spacer(modifier = modifier.width(36.dp))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(circularProgressSize.toDp(context).dp)
        ) {
            _circular_progress_indicator(
                modifier = modifier.wrapContentSize(),
                enable = enable,
                value = virtualizerValue.toInt(),
                maxValue = maxVirtualizerValue.toInt(),
                primaryColor = MaterialTheme.colorScheme.primary,
                secondaryColor = MaterialTheme.colorScheme.secondary,
                progressSize = circularProgressSize.toDp(context),
                circleRadius = circularProgressSize * 0.98f.toDp(context),
                onValueChange = onVirtualizeValueChange
            )
            Spacer(modifier = Modifier.height(4.dp))
            AutoSizeText(
                text = stringResource(id = R.string.virtualize),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.titleSmall.copy(
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        _bar_visualizer(
            modifier = Modifier
                .width(28.dp)
                .height(180.dp)
                .background(Color(0x40000000)),
            resolution = 1,
            visualizationData = visualizerData
        )
    }
}
