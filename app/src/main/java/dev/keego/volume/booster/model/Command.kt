package dev.keego.volume.booster.model

sealed class Command() {
    object Play : Command()
    object Pause : Command()
    object Previous : Command()
    object Next : Command()
}