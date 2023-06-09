package dev.keego.volume.booster

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(
            object : Timber.DebugTree() {
                override fun log(
                    priority: Int,
                    tag: String?,
                    message: String,
                    t: Throwable?,
                ) {
                    super.log(priority, "keego_$tag", "$tag >> $message", t)
                }
            },
        )
        Timber.d("Timber is initialized.")
    }
}
