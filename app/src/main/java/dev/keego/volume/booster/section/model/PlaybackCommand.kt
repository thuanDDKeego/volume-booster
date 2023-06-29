package dev.keego.volume.booster.section.model

sealed class PlaybackCommand() {
    object ContentClick : PlaybackCommand()
    object Play : PlaybackCommand()
    object Pause : PlaybackCommand()
    object Previous : PlaybackCommand()
    object Next : PlaybackCommand()
}
