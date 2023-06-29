package dev.keego.volume.booster.setup.preference

import android.annotation.SuppressLint
import dev.keego.volume.booster.BuildConfig

@SuppressLint("StaticFieldLeak")
object LanguagePreferences :
    PreferencesAdapterRC(name = "language_preferences", BuildConfig.DEBUG) {
    var language by stringPref(defaultValue = "English")
}
