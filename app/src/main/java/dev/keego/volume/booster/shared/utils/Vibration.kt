package dev.keego.volume.booster.shared.utils

import android.annotation.SuppressLint
import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator

object Vibration {
    // strength is a value between 0 and 1
    @SuppressLint("ServiceCast")
    fun vibrate(context: Context, strength: Float, duration: Long = 200) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
        vibrator?.let {
            if (it.hasVibrator()) {
                // Ensure the strength value is in the range 0 to 1
                val safeStrength = strength.coerceIn(0f, 1f)

                // Convert the strength to a value between 1 and 255
                val amplitude = (safeStrength * 255).toInt().coerceIn(1, 255)
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    // API 26 and above
                    it.vibrate(VibrationEffect.createOneShot(duration, amplitude))
                } else {
                    // Below API 26
                    // The amplitude is not controllable on API levels below 26
                    // The device will vibrate for 200 milliseconds with the default amplitude
                    @Suppress("DEPRECATION")
                    it.vibrate(duration)
                }
            }
        }
    }
}
