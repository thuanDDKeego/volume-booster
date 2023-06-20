package dev.keego.volume.booster.repositories

import android.content.Context
import android.media.AudioManager
import android.os.Handler
import dev.keego.volume.booster.services.messages.ServiceCommand
import dev.keego.volume.booster.services.volumeboost.ServiceDispatcher
import dev.keego.volume.booster.services.volumeboostv2.SettingsContentObserver
import kotlin.math.roundToInt
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.greenrobot.eventbus.EventBus
import timber.log.Timber

class BoostServiceRepository(
    private val context: Context
) {
    lateinit var mSettingsContentObserver: SettingsContentObserver

    private val _enable = MutableStateFlow(false)
    val enable = _enable.asStateFlow()

    // init value follow current value volume
    private val _db = MutableStateFlow<Int>(getCurrentValue())
    val db = _db.asStateFlow()
    private val _bandValue = MutableStateFlow<List<Pair<Int, Int>>>(listOf())
    val bandValue = _bandValue.asStateFlow()
    private val _visualizerArray = MutableStateFlow<Int>(128)
    val visualizerArray = _visualizerArray.asStateFlow()

    init {
        initSettingContentObserver()
    }

    // this observer when volume system change
    private fun initSettingContentObserver() {
        mSettingsContentObserver =
            // this current volume is system volume, ex: range from 0 .. 15
            SettingsContentObserver(context, Handler()) { currentVolumeValue ->
                val maxVolumeValue = getMaxVolume()
                val currentValueToPercent =
                    ((currentVolumeValue.toFloat() / maxVolumeValue.toFloat()) * 100f).roundToInt()
                updateBoostValue(currentValueToPercent)
            }
        val uri = android.provider.Settings.System.CONTENT_URI
        // register contentObserver
        context.contentResolver.registerContentObserver(uri, true, mSettingsContentObserver)
    }

    /* input range from 0 to 4 or 500
    * if 0 .. 100: adjust volume device
    * if > 100: adjust volume device to max, then start boost
    */
    fun updateBoostValue(value: Int) {
        _db.value = value
        val maxVolumeValue = getMaxVolume()
        if (value <= 100) {
            val newVolumeValue =
                ((value.toFloat() / 100f) * (maxVolumeValue.toFloat())).roundToInt()
            adjustVolumeSetting(newVolumeValue)
        } else {
            // when > 100%, start boost volume
            // first, adjust volume to max value
            adjustVolumeSetting(maxVolumeValue)
            // start update boost service
        }
        updateDb()
    }

    // this value range from 0 to max volume of system, ex: 0..15
    private fun adjustVolumeSetting(value: Int) {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager.setStreamVolume(
            AudioManager.STREAM_MUSIC, // Stream type
            value, // Volume index
            AudioManager.FLAG_SHOW_UI // Flags
        )
    }

    private fun getCurrentValue(): Int {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val maxVolumeValue = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        val currentVolumeValue = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        return ((currentVolumeValue.toFloat() / maxVolumeValue.toFloat()) * 100f).roundToInt()
    }

    private fun getMaxVolume(): Int {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        return audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
    }

    fun toggleEnableService(context: Context, enable: Boolean) {
        _enable.value = enable
        if (enable) {
            ServiceDispatcher.startService(context)
        } else {
            ServiceDispatcher.stopService(context)
        }
    }

    /* this value range is from 0 .. 400 or more, and
    * 0 is mute
    * 1 to 100 is default volume
    * more 100, start boost
    * */
    fun updateDb() {
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
        Timber.d(
            "updateBandValue ${
                _bandValue.value.joinToString {
                    "${it.first}: ${it.second} ---"
                }
            }"
        )
        try {
            EventBus.getDefault().post(ServiceCommand.UPDATE_HERTZ)
        } catch (ignored: NumberFormatException) {
            print(ignored)
        }
    }

    fun fetchFrequencies(frequencies: List<Pair<Int, Int>>) {
        _bandValue.value = frequencies
    }

    fun updateVisualizerArray(value: Int) {
        _visualizerArray.value = value
    }

    fun unRegisterVolumeObserverContent() {
        if (::mSettingsContentObserver.isInitialized) {
            context.contentResolver.unregisterContentObserver(mSettingsContentObserver)
        }
    }
}
