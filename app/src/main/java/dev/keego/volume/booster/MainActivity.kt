package dev.keego.volume.booster

import android.content.Intent
import android.os.Bundle
import android.provider.Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import dev.keego.volume.booster.screens.home.home_
import dev.keego.volume.booster.services.messages.QueryReplyPing
import dev.keego.volume.booster.services.messages.ServiceQueryPing
import dev.keego.volume.booster.services.volumeboost.GlobalVars
import dev.keego.volume.booster.ui.theme.VolumeBoosterTheme
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requirePermission()
        setContent {
//            val requestLauncher = registerForActivityResult()
            VolumeBoosterTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    home_()
                }
            }
        }
    }

    private fun requirePermission() {
        if (!isNotificationServiceEnabled()) {
            val intent = Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS)
            startActivity(intent)
        }
    }

    private fun isNotificationServiceEnabled(): Boolean {
//        val pkgName = packageName
//        val flat = Settings.Secure.getString(contentResolver, ENABLED_NOTIFICATION_LISTENERS)
//        if (!TextUtils.isEmpty(flat)) {
//            val names = flat.split(":")
//            for (i in names.indices) {
//                val cn = ComponentName.unflattenFromString(names[i])
//                if (cn != null) {
//                    if (TextUtils.equals(pkgName, cn.packageName)) {
//                        return true
//                    }
//                }
//            }
//        }
        return false
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
        EventBus.getDefault().post(ServiceQueryPing())
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
        super.onStop()
    }

    @Subscribe
    fun handlePingReply(reply: QueryReplyPing?) {
        if (GlobalVars.DEBUG_TOAST) Toast.makeText(this, "Ping reply", Toast.LENGTH_SHORT).show()
    }
}
