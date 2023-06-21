package dev.keego.volume.booster.services.volumeboost

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.SharedPreferences
import android.media.audiofx.Equalizer
import android.media.audiofx.LoudnessEnhancer
import android.os.IBinder
import android.preference.PreferenceManager
import android.widget.Toast
import androidx.core.app.NotificationCompat
import dagger.hilt.android.AndroidEntryPoint
import dev.keego.volume.booster.MainActivity
import dev.keego.volume.booster.R
import dev.keego.volume.booster.repositories.BoostServiceRepository
import dev.keego.volume.booster.services.messages.QueryReplyOn
import dev.keego.volume.booster.services.messages.QueryReplyPing
import dev.keego.volume.booster.services.messages.ServiceCommand
import dev.keego.volume.booster.services.messages.ServiceQueryOn
import dev.keego.volume.booster.services.messages.ServiceQueryPing
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.android.BuildConfig
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class VolumeBoostService : Service() {

    companion object {
        private const val TAG = "VolumeBoostService"
        private const val FOREGROUND_NOTIFICATION = 1
        private const val CODE_STOP = 1
    }

    @Inject
    lateinit var boostServiceRepository: BoostServiceRepository

    private var enhancer: LoudnessEnhancer? = null
    private lateinit var sharedPreferences: SharedPreferences
    private var on = true

    private var loudness: Int = 0

    private var equalizer: Equalizer? = null

    override fun onCreate() {
        super.onCreate()
//        AppForegroundReceiver.register(this)
        EventBus.getDefault().register(this)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        enhancer = LoudnessEnhancer(0)
        enhancer?.setTargetGain(boostServiceRepository.db.value)
        enhancer?.enabled = true

        equalizer = Equalizer(0, 0)
        equalizer?.enabled = true

        val frequencies = getAllAdjustableFrequencies()
        val defaultBandLevels = getDefaultBandLevels()
        boostServiceRepository.fetchFrequencies(
            frequencies.mapIndexed { index, it ->
                Pair(
                    it,
                    defaultBandLevels[index],
                )
            },
        )
        frequencies.forEachIndexed { index, frequency ->
            Timber.d("frequencies $index $frequency -- ${defaultBandLevels[index]}")
        }
        Timber.d("frequencies ${frequencies.joinToString { "$it " }}")
    }

    override fun onDestroy() {
        enhancer?.setTargetGain(0)
        enhancer?.release()

        equalizer?.release()

        if (GlobalVars.DEBUG_TOAST) {
            Toast.makeText(this, "Service stopped", Toast.LENGTH_SHORT).show()
        }
        EventBus.getDefault().unregister(this)
        super.onDestroy()
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

    // Lấy vị trí của tần số trong danh sách tần số
    fun getBandIndex(frequency: Int): Short {
        return equalizer?.getBand(frequency) ?: 0
    }

    // Tăng cường tần số cụ thể
    fun boostFrequency(frequency: Int, gain: Int) {
        val bandIndex = getBandIndex(frequency)
        equalizer?.setBandLevel(bandIndex, gain.toShort())
    }

    // Giảm mức độ tần số cụ thể
    fun reduceFrequency(frequency: Int, gain: Int) {
        val bandIndex = getBandIndex(frequency)
        equalizer?.setBandLevel(bandIndex, (-gain).toShort())
    }

    private fun getDefaultBandLevels(): IntArray {
        val bandCount = equalizer?.numberOfBands ?: 0
        val defaultBandLevels = IntArray(bandCount.toInt())

        for (i in 0 until bandCount) {
            val centerFrequency = equalizer?.getCenterFreq(i.toShort())
            val levelRange = equalizer?.getBandLevelRange()

            centerFrequency?.let { frequency ->
                levelRange?.let { range ->
                    val defaultLevel =
                        (range[0] + range[1]) / 2 // Giá trị mặc định nằm ở giữa khoảng mức độ tần số
                    defaultBandLevels[i.toInt()] = defaultLevel
                }
            }
        }

        return defaultBandLevels
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
                    boostFrequency(it.first, it.second)
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
            else -> {}
        }
        if (GlobalVars.DEBUG_TOAST) {
            Toast.makeText(this, "Received command: $command", Toast.LENGTH_SHORT).show()
        }
    }

    @Subscribe
    fun handlePingQuery(event: ServiceQueryPing) {
        EventBus.getDefault().post(QueryReplyPing())
        if (GlobalVars.DEBUG_TOAST) {
            Toast.makeText(this, "Ping Query", Toast.LENGTH_SHORT).show()
        }
    }

    @Subscribe
    fun handleOnQuery(query: ServiceQueryOn) {
        EventBus.getDefault().post(QueryReplyOn(on))
    }

    private fun startForeground() {
        var flag = 0
        flag = PendingIntent.FLAG_IMMUTABLE
        val notification = getNotification(flag)
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

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground()
        if (BuildConfig.DEBUG) {
            Timber.tag(TAG)
                .i("onStartCommand() called with intent = [ $intent ], flags = [ $flags ], startId = [ $startId ]")
        }
        if (GlobalVars.DEBUG_TOAST) {
            Toast.makeText(
                this,
                "onStartCommand() called with " +
                    "intent = [$intent], flags = [$flags], startId = [$startId]",
                Toast.LENGTH_LONG,
            ).show()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}
