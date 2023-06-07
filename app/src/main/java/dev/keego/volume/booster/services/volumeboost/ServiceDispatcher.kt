package dev.keego.volume.booster.services.volumeboost

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.audiofx.AudioEffect
import android.widget.Toast
import androidx.core.content.ContextCompat
import dev.keego.volume.booster.services.messages.AudioSessionEvent
import dev.keego.volume.booster.services.messages.ServiceCommand
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.android.BuildConfig
import timber.log.Timber

class ServiceDispatcher : BroadcastReceiver() {
    private val TAG = "ServiceDispatcher"

    companion object {
        fun startService(context: Context) {
            val service = Intent(context.applicationContext, VolumeBoostService::class.java)
            ContextCompat.startForegroundService(context, service)
        }

        fun stopService(context: Context) {
            EventBus.getDefault().post(ServiceCommand.STOP)
        }
    }

    @SuppressLint("BinaryOperationInTimber")
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action ?: return
        val open: Boolean = when (action) {
            AudioEffect.ACTION_OPEN_AUDIO_EFFECT_CONTROL_SESSION -> true
            AudioEffect.ACTION_CLOSE_AUDIO_EFFECT_CONTROL_SESSION -> false
            else -> return
        }
        val sessionId = intent.getIntExtra(AudioEffect.EXTRA_AUDIO_SESSION, 0)
        val pkg = intent.getStringExtra(AudioEffect.EXTRA_PACKAGE_NAME)

        EventBus.getDefault().post(AudioSessionEvent(open, AudioSessionInfo(sessionId, pkg!!)))

        if (GlobalVars.DEBUG_TOAST) {
            Toast.makeText(
                context,
                "${intent.getIntExtra(AudioEffect.EXTRA_AUDIO_SESSION, 0)}, ${
                    intent.getStringExtra(
                        AudioEffect.EXTRA_PACKAGE_NAME,
                    )
                }",
                Toast.LENGTH_LONG,
            ).show()
        }

        if (BuildConfig.DEBUG) {
            Timber.tag(TAG).d(
                intent.getIntExtra(
                    AudioEffect.EXTRA_AUDIO_SESSION,
                    0,
                ).toString() + ", " + intent.getStringExtra(AudioEffect.EXTRA_PACKAGE_NAME),
            )
        }
    }
}
