package dev.keego.volume.booster.shared.extensions

import android.content.Context
import android.util.LayoutDirection
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale

@Stable
fun Modifier.mirrorable(context: Context): Modifier {
    return if (context.resources.configuration.layoutDirection == LayoutDirection.RTL) {
        this.scale(scaleX = -1f, scaleY = 1f)
    } else {
        this
    }
}
