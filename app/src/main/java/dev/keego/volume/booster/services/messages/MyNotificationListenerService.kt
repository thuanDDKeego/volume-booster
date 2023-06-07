package dev.keego.volume.booster.services.messages

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.app.Notification
import android.os.Bundle

class MyNotificationListenerService : NotificationListenerService() {

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        val notification: Notification = sbn.notification
        val extras: Bundle = notification.extras
        val title: String? = extras.getString(Notification.EXTRA_TITLE)
        val text: CharSequence? = extras.getCharSequence(Notification.EXTRA_TEXT)
        val textString: String = text?.toString() ?: ""

        // Do something with these notification details
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        // Handle the removed notification if necessary
    }
}
