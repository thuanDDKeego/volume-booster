package dev.keego.volume.booster.setup.preference

import android.annotation.SuppressLint
import dev.keego.volume.booster.BuildConfig

@SuppressLint("StaticFieldLeak")
object AppPreferences : PreferencesAdapterRC(name = "app_preferences", BuildConfig.DEBUG) {
    var usageCounter by intPref(defaultValue = 0)
    var enableMusic by booleanPref(defaultValue = true)

    var isChatTutorialShowed by booleanPref(defaultValue = false)
    var isLevelTutorialShowed by booleanPref(defaultValue = false)
    var isSwipeTutorialShowed by booleanPref(defaultValue = false)
}
