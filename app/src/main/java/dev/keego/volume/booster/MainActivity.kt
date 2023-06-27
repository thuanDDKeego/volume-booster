package dev.keego.volume.booster

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.provider.Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.rememberNavHostEngine
import dagger.hilt.android.AndroidEntryPoint
import dev.keego.volume.booster.screens.NavGraphs
import dev.keego.volume.booster.screens.home.home_
import dev.keego.volume.booster.services.messages.QueryReplyPing
import dev.keego.volume.booster.services.messages.ServiceQueryPing
import dev.keego.volume.booster.services.volumeboost.GlobalVars
import dev.keego.volume.booster.ui.theme.VolumeBoosterTheme
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requirePermission()
        ensureNotificationAccess()
        requireRecordAudio(this)
        setContent {
//            val requestLauncher = registerForActivityResult()
            VolumeBoosterTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    home_()
                }
                val engine = rememberNavHostEngine()
                val navController = engine.rememberNavController().apply {
                    addOnDestinationChangedListener { _, destination, _ ->
                        Timber.v("navigate -> ${destination.route}")
//                        InternetRequiredHelper.required(this@MainActivity)
                    }
                }
                Scaffold {
                    DestinationsNavHost(
                        modifier = Modifier,
                        navGraph = NavGraphs.root,
                        engine = engine,
                        navController = navController
                    )
                }
            }
        }
    }

    private fun requireRecordAudio(activity: Activity) {
        if (ContextCompat.checkSelfPermission(
                activity,
                android.Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(android.Manifest.permission.RECORD_AUDIO),
                42
            )
        }
    }

    private fun requirePermission() {
        if (!isNotificationServiceEnabled(context = this)) {
            val intent = Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS)
            startActivity(intent)
        }
    }

//    private fun isNotificationServiceEnabled(context: Context): Boolean {
//        val enabledListeners = Settings.Secure.getString(
//            context.contentResolver,
//            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES,
//        )
//        val packageName = context.packageName
//        if (!TextUtils.isEmpty(enabledListeners)) {
//            val listeners = enabledListeners.split(":")
//            for (listener in listeners) {
//                val componentName = ComponentName.unflattenFromString(listener)
//                if (componentName != null && TextUtils.equals(
//                        packageName,
//                        componentName.packageName,
//                    )
//                ) {
//                    return true
//                }
//            }
//        }
//        return false
//    }

    private fun isNotificationServiceEnabled(context: Context): Boolean {
        val packageName = context.packageName
        val enabledListeners = NotificationManagerCompat.getEnabledListenerPackages(context)

        return enabledListeners.contains(packageName)
    }

    private fun ensureNotificationAccess() {
        val notificationListenerEnabled = Settings.Secure.getString(
            contentResolver,
            "enabled_notification_listeners"
        ).contains(applicationContext.packageName)

        if (!notificationListenerEnabled) {
            val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
            startActivity(intent)
        }
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
