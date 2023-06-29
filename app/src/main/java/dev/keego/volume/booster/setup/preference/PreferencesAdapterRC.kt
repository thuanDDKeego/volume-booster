package dev.keego.volume.booster.setup.preference

import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import timber.log.Timber

abstract class PreferencesAdapterRC(
    name: String? = null,
    private val devMode: Boolean = false
) : Preferences(name) {
    private val remoteConfig = Firebase.remoteConfig

    init {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = if (devMode) 0 else 3600
        }
        remoteConfig.apply {
            setConfigSettingsAsync(configSettings)
            setDefaultsAsync(_defaults)
            fetchAndActivate().addOnCompleteListener {
                if (it.isSuccessful) {
                    try {
                        Timber.d("Remote config result: ${it.result}")

                        if (it.result) {
                            remoteConfig.all.forEach { entry ->
                                Timber.d(
                                    "Remote Config fetched: ${entry.key} - ${entry.value.asString()}"
                                )
                            }
                            syncRemoteConfigToPreferences()
                        }
                    } catch (e: Exception) {
                        Firebase.crashlytics.recordException(e)
                    }
                }
            }
        }
    }

    private fun syncRemoteConfigToPreferences() = try {
        prefs.edit().apply {
            // for now, all remote config must be snake case
            // to make this function works
            // TODO: we need to update PreferencesAdapterRC support as enum Type
            remoteConfig.all.forEach {
                val key = it.key
                if (_defaults.containsKey(key)) {
                    when (_defaults[key]) {
                        is Boolean -> {
                            val value = remoteConfig.getBoolean(key)
                            putBoolean(key, value)
//                        _defaults[key] = value
                        }

                        is String -> {
                            val value = remoteConfig.getString(key)
                            putString(key, value)
//                        _defaults[key] = value
                        }

                        is Int -> {
                            val value = remoteConfig.getLong(key).toInt()
                            putInt(key, value)
//                        _defaults[key] = value
                        }

                        is Float -> {
                            val value = remoteConfig.getDouble(key).toFloat()
                            putFloat(key, value)
//                        _defaults[key] = value
                        }

                        is Long -> {
                            val value = remoteConfig.getLong(key)
                            putLong(key, value)
//                        _defaults[key] = value
                        }
                    }
                }
                apply()
            }
        }
    } catch (e: Exception) {
        Timber.e(e)
        Firebase.crashlytics.recordException(e)
    }
}
