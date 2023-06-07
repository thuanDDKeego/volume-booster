package dev.keego.volume.booster.repositories

import android.content.Context
import android.media.audiofx.LoudnessEnhancer
import dev.keego.volume.booster.services.messages.ServiceCommand
import dev.keego.volume.booster.services.volumeboost.ServiceDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.greenrobot.eventbus.EventBus

class BoostServiceRepository {

    private val _enable = MutableStateFlow(false)
    val enable = _enable.asStateFlow()
    private val _db = MutableStateFlow<Int>(0)
    val db = _db.asStateFlow()
    fun toggleEnableService(context: Context, enable: Boolean) {
        _enable.value = enable
        if (enable) {
            ServiceDispatcher.startService(context)
        } else {
            ServiceDispatcher.stopService(context)
        }
    }

    val enhancer = LoudnessEnhancer(0).also {
        it.enabled = true
    }

    fun updateDb(db: Int) {
        _db.value = db
        try {
            EventBus.getDefault().post(ServiceCommand.UPDATE)
        } catch (ignored: NumberFormatException) {
            print(ignored)
        }
    }
}
