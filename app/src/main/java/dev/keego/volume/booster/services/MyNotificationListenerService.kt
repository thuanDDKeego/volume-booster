package dev.keego.volume.booster.services

import android.app.Notification
import android.app.PendingIntent
import android.media.session.MediaController
import android.media.session.MediaSession
import android.media.session.PlaybackState
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import dagger.hilt.android.AndroidEntryPoint
import dev.keego.volume.booster.repositories.NotificationPlaybackRepository
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MyNotificationListenerService : NotificationListenerService() {
    private val NOTIFICATION_TAG = "NOTIFICATION_TAG"
    var action: Notification.Action? = null
    @Inject lateinit var notificationPlaybackRepository: NotificationPlaybackRepository


    override fun onCreate() {
        super.onCreate()
        // Get all current notifications
        val currentNotifications = activeNotifications

        for (notification in currentNotifications) {
            // Do something with the notification
            val extras = notification.notification.extras
            val title = extras.getString(Notification.EXTRA_TITLE)
            val text = extras.getCharSequence(Notification.EXTRA_TEXT)?.toString()
            Timber.d("NOTIFICATION_TAG title: $title --- extras: $extras --- notification $notification")
            // Process the notification information as per your need
        }
    }


    override fun onNotificationPosted(sbn: StatusBarNotification) {
        val notification = sbn.notification
        val extras = notification.extras
        val title = extras.getString(Notification.EXTRA_TITLE)
        val text = extras.getCharSequence(Notification.EXTRA_TEXT).toString()
        val actions = notification.actions
        val token =
            extras.getParcelable<MediaSession.Token>(Notification.EXTRA_MEDIA_SESSION)
        val mediaController: MediaController? = token?.let { MediaController(this, it) }
        val playbackState = mediaController?.playbackState
        Timber.d("NOTIFICATION_TAG posted title: $title --- actions size: ${actions?.size} --- token: $token -- playback ${playbackState?.state}")

        if (action != null) return
        token?.let {
            val mediaController = MediaController(this, it)
            val playbackState = mediaController.playbackState

            if (playbackState != null) {
                when (playbackState.state) {
                    PlaybackState.STATE_PLAYING -> {
                        // Handle playing state
                        Timber.d("NOTIFICATION_TAG state title: $title --- state_playing")
                    }

                    PlaybackState.STATE_STOPPED -> {
                        // Handle stoped state
                        Timber.d("NOTIFICATION_TAG state title: $title --- state_stoped")

                    }

                    PlaybackState.STATE_PAUSED -> {
                        // Handle paused state
                        Timber.d("NOTIFICATION_TAG state title: $title --- state_pause")

                    }

                    else -> {
                        Timber.d("NOTIFICATION_TAG state title: $title --- $it")
                        // Handle other states if necessary
                    }
                }
            }
            actions?.let {
                // action play pause thường ở giữa (ex: 5 -> 3, 3 -> 1)
                if (actions.size != 3 && actions.size != 5) return
                action = actions[actions.size / 2]
//                if (actionTitle.contains("play") || actionTitle.contains("pause")) {
//                    // Trigger the PendingIntent associated with this action
                try {
                    Timber.d("NOTIFICATION_TAG prepare action: title: $title --- extras: $extras --- notification $notification")
//                    CoroutineScope(Dispatchers.IO).launch {
//                        var counter = 0
//                        while (counter <= 10) {
//                            delay(5000)
//                            action!!.actionIntent.send()
//                            counter++
//                        }
//                    }
                } catch (e: PendingIntent.CanceledException) {
                    e.printStackTrace()
                }
//                }
//            }

                // Do something with these notification details
            }
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        // Handle the removed notification if necessary
        val notification = sbn.notification
        val extras = notification.extras
        val title = extras.getString(Notification.EXTRA_TITLE)
        val text = extras.getCharSequence(Notification.EXTRA_TEXT).toString()
        Timber.d("NOTIFICATION_TAG removed title: $title --- extras: $extras --- notification $notification")

    }
}