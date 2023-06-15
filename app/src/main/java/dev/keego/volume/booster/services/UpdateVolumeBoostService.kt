package dev.keego.volume.booster.services

import android.app.*
import android.content.Intent
import android.content.SharedPreferences
import android.media.audiofx.Equalizer
import android.media.audiofx.LoudnessEnhancer
import android.media.audiofx.Visualizer
import android.os.IBinder
import android.preference.PreferenceManager
import android.widget.Toast
import androidx.core.app.NotificationCompat
import dagger.hilt.android.AndroidEntryPoint
import dev.keego.volume.booster.BuildConfig
import dev.keego.volume.booster.MainActivity
import dev.keego.volume.booster.R
import dev.keego.volume.booster.repositories.BoostServiceRepository
import dev.keego.volume.booster.services.messages.*
import dev.keego.volume.booster.services.volumeboost.GlobalVars
import dev.keego.volume.booster.services.volumeboost.NotificationChannelHelper
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class UpdateVolumeBoostService : Service() {

    companion object {
        private const val TAG = "VolumeBoostService"
        private const val FOREGROUND_NOTIFICATION = 1
    }

    @Inject
    lateinit var boostServiceRepository: BoostServiceRepository

    private var enhancer: LoudnessEnhancer? = null
    private lateinit var sharedPreferences: SharedPreferences
    private var on = true
    private var equalizer: Equalizer? = null
    private var visualizer: Visualizer? = null

    override fun onCreate() {
        super.onCreate()
        EventBus.getDefault().register(this)
        initAudioSettings()
        setupDefaultFrequencies()
    }

    override fun onDestroy() {
        cleanUp()
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground()
        if (BuildConfig.DEBUG) {
            Timber.tag(TAG).i("onStartCommand() called with intent = [ $intent ], flags = [ $flags ], startId = [ $startId ]")
        }
        if (GlobalVars.DEBUG_TOAST) {
            showToast("onStartCommand() called with intent = [$intent], flags = [$flags], startId = [$startId]")
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun initAudioSettings() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        enhancer = LoudnessEnhancer(0).apply {
            setTargetGain(boostServiceRepository.db.value)
            enabled = true
        }
        equalizer = Equalizer(0, 0).apply { enabled = true }
        visualizer = Visualizer(0).apply {
            enabled = false // All configuration have to be done in a disabled state
            captureSize = Visualizer.getCaptureSizeRange()[0] // Minimum sampling
            setDataCaptureListener(
                object : Visualizer.OnDataCaptureListener {
                    override fun onFftDataCapture(visualizer: Visualizer, fft: ByteArray, samplingRate: Int) {
                        // Here, you can add your FFT data processing
                    }

                    override fun onWaveFormDataCapture(visualizer: Visualizer, waveform: ByteArray, samplingRate: Int) {
                        processWaveform(waveform)
                    }
                },
                Visualizer.getMaxCaptureRate(), true, true)
            enabled = true // Configuration is done, can enable now...
        }
    }

    private fun setupDefaultFrequencies() {
        val frequencies = getAllAdjustableFrequencies()
        val defaultBandLevels = getDefaultBandLevels()
        boostServiceRepository.fetchFrequencies(frequencies.mapIndexed { index, it -> Pair(it, defaultBandLevels[index]) })
        frequencies.forEachIndexed { index, frequency ->
            Timber.d("frequencies $index $frequency -- ${defaultBandLevels[index]}")
        }
        Timber.d("frequencies ${frequencies.joinToString { "$it " }}")
    }

    private fun cleanUp() {
        enhancer?.apply {
            setTargetGain(0)
            release()
        }
        equalizer?.release()

        if (GlobalVars.DEBUG_TOAST) {
            showToast("Service stopped")
        }
    }

    private fun processWaveform(waveform: ByteArray) {
        // Here, you can add your waveform processing code
        Timber.d("waveform $waveform")
    }

    private fun getAllAdjustableFrequencies(): MutableList<Int> {
        val adjustableFrequencies = mutableListOf<Int>()
        val bandCount = equalizer?.numberOfBands ?: 0
        for (i in 0 until bandCount) {
            val centerFrequency = equalizer?.getCenterFreq(i.toShort())
            centerFrequency?.let { frequency ->
                adjustableFrequencies.add(frequency)
            }
        }
        return adjustableFrequencies
    }

    private fun getDefaultBandLevels(): IntArray {
        val bandCount = equalizer?.numberOfBands ?: 0
        val defaultBandLevels = IntArray(bandCount.toInt())
        for (i in 0 until bandCount) {
            val centerFrequency = equalizer?.getCenterFreq(i.toShort())
            val levelRange = equalizer?.getBandLevelRange()

            centerFrequency?.let { frequency ->
                levelRange?.let { range ->
                    val defaultLevel = (range[0] + range[1]) / 2
                    defaultBandLevels[i.toInt()] = defaultLevel
                }
            }
        }
        return defaultBandLevels
    }

    private fun getBandIndex(frequency: Int): Short {
        return equalizer?.getBand(frequency) ?: 0
    }

    private fun adjustFrequency(frequency: Int, gain: Int) {
        val bandIndex = getBandIndex(frequency)
        equalizer?.setBandLevel(bandIndex, gain.toShort())
    }

    @Subscribe
    fun handleCommands(command: ServiceCommand) {
        when (command) {
            ServiceCommand.STOP -> {
                Timber.d("handleCommands command stop")
                stopSelf()
            }

            ServiceCommand.UPDATE -> {
                Timber.d("handleCommands command update")
                enhancer?.setTargetGain(boostServiceRepository.db.value)
                updateNotification()
            }

            ServiceCommand.UPDATE_HERTZ -> {
                Timber.d("handleCommands command update")
                boostServiceRepository.bandValue.value.forEach {
                    adjustFrequency(it.first, it.second)
                }
            }

            ServiceCommand.PLAY -> {
                Timber.d("handleCommands command play")
                enhancer?.enabled = true
                on = true
                updateNotification()
            }

            ServiceCommand.PAUSE -> {
                Timber.d("handleCommands command pause")
                enhancer?.enabled = false
                on = false
                updateNotification()
            }
        }
        if (GlobalVars.DEBUG_TOAST) {
            showToast("Received command: $command")
        }
    }

    @Subscribe
    fun handlePingQuery(event: ServiceQueryPing) {
        EventBus.getDefault().post(QueryReplyPing())
        if (GlobalVars.DEBUG_TOAST) {
            showToast("Ping Query")
        }
    }

    @Subscribe
    fun handleOnQuery(query: ServiceQueryOn) {
        EventBus.getDefault().post(QueryReplyOn(on))
    }

    private fun startForeground() {
        val notification = getNotification(PendingIntent.FLAG_IMMUTABLE)
        NotificationChannelHelper.createNotificationChannel(this)
        startForeground(FOREGROUND_NOTIFICATION, notification)
    }

    private fun getNotification(flag: Int): Notification {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, flag)
        return NotificationCompat.Builder(this, NotificationChannelHelper.SERVICE_CHANNEL)
            .setContentTitle("Volume Boost Service")
            .setContentText(if (on) "Loudness Boost: ${boostServiceRepository.db.value / 10.0}db" else "Paused")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .build()
    }

    private fun updateNotification() {
        val notification = getNotification(PendingIntent.FLAG_IMMUTABLE)
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.notify(FOREGROUND_NOTIFICATION, notification)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
