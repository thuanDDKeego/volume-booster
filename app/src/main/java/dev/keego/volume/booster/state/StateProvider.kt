package dev.keego.volume.booster.state

import androidx.compose.runtime.compositionLocalOf

class PreviewableState() : State

val LocalState = compositionLocalOf<VimelStateHolder<out State>> {
    VimelStateHolder(PreviewableState())
}

private fun noLocalProvidedFor(name: String): Nothing {
    error("CompositionLocal $name not present")
}
