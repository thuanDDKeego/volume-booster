package dev.keego.volume.booster.services.volumeboostv2

import android.content.Context
import android.database.ContentObserver
import android.media.AudioManager
import android.os.Handler
import androidx.core.content.ContextCompat.getSystemService
import timber.log.Timber

class SettingsContentObserver(
    private val context: Context,
    handler: Handler,
    private val onVolumeChange: (current: Int) -> Unit
) : ContentObserver(handler) {

    companion object {
        // maybe update singleton here
    }

    private val audioManager: AudioManager =
        context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    override fun onChange(selfChange: Boolean) {
        val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        Timber.d("Volume change Volume now $currentVolume $maxVolume")
        onVolumeChange(currentVolume)
    }
}
