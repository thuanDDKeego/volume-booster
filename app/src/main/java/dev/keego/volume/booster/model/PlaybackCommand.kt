package dev.keego.volume.booster.model

sealed class PlaybackCommand() {
    object ContentClick : PlaybackCommand()
    object Play : PlaybackCommand()
    object Pause : PlaybackCommand()
    object Previous : PlaybackCommand()
    object Next : PlaybackCommand()
}
