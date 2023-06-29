package dev.keego.volume.booster.services

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Icon
import android.media.session.MediaController
import android.media.session.MediaSession
import android.media.session.PlaybackState
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import dagger.hilt.android.AndroidEntryPoint
import dev.keego.volume.booster.section.model.PlaybackCommand
import dev.keego.volume.booster.section.repositories.NotificationPlaybackRepository
import dev.keego.volume.booster.section.repositories.PlayBackState
import java.util.Locale
import javax.inject.Inject
import kotlin.math.abs
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class NotificationHandlePlaybackService : NotificationListenerService() {
    private var contentAction: PendingIntent? = null
    private var playPauseAction: Notification.Action? = null
    private var previousAction: Notification.Action? = null
    private var nextAction: Notification.Action? = null

    @Inject
    lateinit var notificationPlaybackRepository: NotificationPlaybackRepository

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate() {
        super.onCreate()
        // Get all current notifications
        val currentNotifications = activeNotifications
        Timber.d(
            "NOTIFICATION_TAG title oncreate: currentNotifications ${currentNotifications.size}}"
        )
        for (notification in currentNotifications) {
            // Do something with the notification
            val extras = notification.notification.extras
            val title = extras.getString(Notification.EXTRA_TITLE)
            val text = extras.getCharSequence(Notification.EXTRA_TEXT)?.toString()
            Timber.d(
                "NOTIFICATION_TAG title oncreate: $title --- extras: $extras --- notification $notification"
            )
            // Process the notification information as per your need
        }
        GlobalScope.launch {
            notificationPlaybackRepository.command.collect {
                try {
                    when (it) {
                        is PlaybackCommand.ContentClick -> {
                            contentAction?.send()
                        }

                        is PlaybackCommand.Play -> {
                            playPauseAction?.actionIntent?.send()
                        }

                        is PlaybackCommand.Pause -> {
                            playPauseAction?.actionIntent?.send()
                        }

                        is PlaybackCommand.Previous -> {
                            previousAction?.actionIntent?.send()
                        }

                        is PlaybackCommand.Next -> {
                            nextAction?.actionIntent?.send()
                        }

                        else -> {}
                    }
                } catch (e: PendingIntent.CanceledException) {
                    e.printStackTrace()
                }

                Timber.d("NOTIFICATION_TAG receive command $it")
            }
        }
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        val notification = sbn.notification
        val extras = notification.extras
        val song = extras.getCharSequence(Notification.EXTRA_TITLE).toString()
        val artist = extras.getCharSequence(Notification.EXTRA_TEXT).toString()
        val smallIcon = extras.getParcelable<Bitmap>(Notification.EXTRA_SMALL_ICON)
        val mainColor = abs(notification.color)
        Timber.d("maincolorr  $mainColor")
        val actions = notification.actions?.onEach { it.actionIntent }
        val token =
            extras.getParcelable<MediaSession.Token>(Notification.EXTRA_MEDIA_SESSION)
        val mediaController: MediaController? = token?.let { MediaController(this, it) }
        val playbackState = mediaController?.playbackState
        Timber.d(
            "NOTIFICATION_TAG posted title: $song --- text: $artist --- actions: [${
                actions?.joinToString {
                    it.title.toString().lowercase(Locale.ENGLISH) + " "
                }
            }] --- token: $token --- playback ${playbackState?.state}"
        )

        token?.let {
            if (playbackState != null) {
                when (playbackState.state) {
                    PlaybackState.STATE_PLAYING -> {
                        // Handle playing state
                        notificationPlaybackRepository.updatePlayBack(
                            PlayBackState(
                                song = song,
                                artist = artist,
                                thumb = notification.getLargeIcon()?.toBitmap(this),
                                color = mainColor,
                                isPlaying = true
                            )
                        )
                        Timber.d("NOTIFICATION_TAG state title: $song --- state_playing")
                    }

                    PlaybackState.STATE_PAUSED -> {
                        // Handle paused state
                        notificationPlaybackRepository.updatePlayBack(
                            PlayBackState(
                                song = song,
                                artist = artist,
                                thumb = notification.getLargeIcon()?.toBitmap(this),
                                color = mainColor,
                                isPlaying = false
                            )
                        )
                        Timber.d("NOTIFICATION_TAG state title: $song --- state_pause")
                    }

                    else -> {
                    }
                }
            }
            actions?.let {
                contentAction = notification.contentIntent
                // action play pause thường ở giữa (ex: 5 -> 3, 3 -> 1)
                if (actions.size < 3) return
                val playPauseActionIndex = actions.map { it.title.toString() }
                    .indexOfFirst { it.lowercase() == "play" || it.lowercase() == "pause" }
                if (playPauseActionIndex >= 1
                ) {
                    Timber.d("actions locale $playPauseActionIndex")
                    playPauseAction = actions[playPauseActionIndex]
                    previousAction = actions[playPauseActionIndex - 1]
                    nextAction = actions[playPauseActionIndex + 1]
                    return
                }
                playPauseAction = actions[actions.size / 2]
                previousAction = actions[actions.size / 2 - 1]
                nextAction = actions[actions.size / 2 + 1]
//                try {
//                    Timber.d("NOTIFICATION_TAG prepare action: title: $song --- extras: $extras --- notification $notification")
//                    CoroutineScope(Dispatchers.IO).launch {
//                        var counter = 0
//                        while (counter <= 10) {
//                            delay(5000)
//                            action!!.actionIntent.send()
//                            counter++
//                        }
//                    }
//                } catch (e: PendingIntent.CanceledException) {
//                    e.printStackTrace()
//                }
//                }
//            }

                // Do something with these notification details
            }
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
//        val notification = sbn.notification
//        val extras = notification.extras
//        val song = extras.getCharSequence(Notification.EXTRA_TITLE).toString()
//        val actions = notification.actions
//        val playBackState = notificationPlaybackRepository.playback.value
//        val resetActions = {
//            playPauseAction = null
//            previousAction = null
//            nextAction = null
//        }
//        if (playBackState.song.contains(song)) {
//            notificationPlaybackRepository.removePlayBack()
//            resetActions()
//        }

//        actions?.let {
//            // action play pause thường ở giữa (ex: 5 -> 3, 3 -> 1)
//            if (actions.size < 3) return
//            if (playPauseAction == actions[actions.size / 2] ||
//                previousAction == actions[actions.size / 2 - 1] ||
//                nextAction == actions[actions.size / 2 + 1]
//            ) {
//                playPauseAction = null
//                previousAction = null
//                nextAction = null
//            }
//        }
    }

    private fun Icon.toBitmap(context: Context): Bitmap? {
        val drawable = loadDrawable(context) ?: return null
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }
}
