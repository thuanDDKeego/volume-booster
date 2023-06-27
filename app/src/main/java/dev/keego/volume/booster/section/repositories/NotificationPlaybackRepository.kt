package dev.keego.volume.booster.section.repositories

import android.app.Notification
import android.graphics.Bitmap
import android.graphics.Color
import androidx.core.graphics.ColorUtils
import androidx.palette.graphics.Palette
import dev.keego.volume.booster.section.model.PlaybackCommand
import kotlin.coroutines.resume
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext

class NotificationPlaybackRepository {
    private val _playPauseAction = MutableStateFlow<Notification.Action?>(null)
    val playPauseAction = _playPauseAction.asStateFlow()

    private val _previousAction = MutableStateFlow<Notification.Action?>(null)
    val previousAction = _previousAction.asStateFlow()

    private val _nextAction = MutableStateFlow<Notification.Action?>(null)
    val nextAction = _nextAction.asStateFlow()

    private val _playback = MutableStateFlow<PlayBackState>(PlayBackState())
    val playback = _playback.asStateFlow()

    private val _color = MutableStateFlow<Int>(0xFF459948.toInt())
    val color = _color.asStateFlow()

    private val _command = MutableSharedFlow<PlaybackCommand?>()
    val command = _command.asSharedFlow()

    fun updatePlayPauseAction(action: Notification.Action?) {
        _playPauseAction.value = action
    }

    fun updatePreviousAction(action: Notification.Action?) {
        _nextAction.value = action
    }

    fun updateNextAction(action: Notification.Action?) {
        _nextAction.value = action
    }

    fun putCommand(command: PlaybackCommand) {
        CoroutineScope(Dispatchers.IO).launch {
            _command.emit(command)
        }
    }

    fun updatePlayBack(playback: PlayBackState) {
        _playback.value = playback
        if (playback.thumb != null) {
            CoroutineScope(Dispatchers.Default).launch {
                val mainColor = getDominantColor(playback.thumb) ?: return@launch
                if (isColorful(mainColor)) {
                    _color.value = ensureDarkColor(mainColor)
                }
//                else {
//                    _color.value = ensureDarkColor(mainColor)
//                }
            }
        }
    }

    fun removePlayBack() {
        _playback.value = PlayBackState()
    }

    suspend fun getDominantColor(bitmap: Bitmap): Int? =
        suspendCancellableCoroutine { continuation ->
            Palette.from(bitmap).generate { palette ->
                // The 'vibrant' color is likely to be the most dominant.
                // You might also want to check 'dominantSwatch' or other swatches.
                val color = palette?.vibrantSwatch?.rgb
//                if (color != null) {
                continuation.resume(color)
//                } else {
//                    continuation.resumeWithException(
//                        IllegalStateException("Cannot access the color!")
//                    )
//                }
            }
        }

    suspend fun ensureDarkColor(color: Int): Int = withContext(Dispatchers.Default) {
        val luminance = ColorUtils.calculateLuminance(color)
        if (luminance < 0.5) {
            // Color is already dark, return as is
            color
        } else {
            // Color is light, darken it
            val hsv = FloatArray(3)
            Color.colorToHSV(color, hsv)
            hsv[2] *= 0.8f // reduce brightness by 20%
            Color.HSVToColor(hsv)
        }
    }

    suspend fun isColorful(color: Int): Boolean = withContext(Dispatchers.Default) {
        val hsv = FloatArray(3)
        Color.colorToHSV(color, hsv)
        hsv[1] > 0.5 // saturation value greater than 0.5 is considered colorful
    }
}

data class PlayBackState(
    val song: String = "Name song",
    val artist: String = "",
    val thumb: Bitmap? = null,
    val color: Int = 0xFF353535.toInt(),
    val isPlaying: Boolean = false
)
