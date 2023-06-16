package dev.keego.volume.booster.repositories

import android.content.Context
import dev.keego.volume.booster.services.messages.ServiceCommand
import dev.keego.volume.booster.services.volumeboost.ServiceDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.greenrobot.eventbus.EventBus
import timber.log.Timber

class BoostServiceRepository {

    private val _enable = MutableStateFlow(false)
    val enable = _enable.asStateFlow()
    private val _db = MutableStateFlow<Int>(0)
    val db = _db.asStateFlow()
    private val _bandValue = MutableStateFlow<List<Pair<Int, Int>>>(listOf())
    val bandValue = _bandValue.asStateFlow()
    private val _visualizerArray = MutableStateFlow<IntArray?>(null)
    val visualizerArray = _visualizerArray.asStateFlow()
    fun toggleEnableService(context: Context, enable: Boolean) {
        _enable.value = enable
        if (enable) {
            ServiceDispatcher.startService(context)
        } else {
            ServiceDispatcher.stopService(context)
        }
    }

    fun updateDb(db: Int) {
        _db.value = db
        try {
            EventBus.getDefault().post(ServiceCommand.UPDATE)
        } catch (ignored: NumberFormatException) {
            print(ignored)
        }
    }

    fun updateBandValue(frequency: Int, bandLevel: Int) {
        Timber.d("updateBandValue join")
        _bandValue.value = _bandValue.value.toMutableList().also { hertz ->
            val index = hertz.indexOfFirst { it.first == frequency }
            if (index < 0) return
            hertz[index] = hertz[index].copy(second = bandLevel)
        }
        Timber.d("updateBandValue ${_bandValue.value.joinToString { "${it.first}: ${it.second} ---" }}")
        try {
            EventBus.getDefault().post(ServiceCommand.UPDATE_HERTZ)
        } catch (ignored: NumberFormatException) {
            print(ignored)
        }
    }

    fun fetchFrequencies(frequencies: List<Pair<Int, Int>>) {
        _bandValue.value = frequencies
    }

    fun updateVisualizerArray(array: IntArray?) {
        _visualizerArray.value = array
    }
}
