package dev.keego.volume.booster

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
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.canopas.lib.showcase.IntroShowCaseState
import com.canopas.lib.showcase.rememberIntroShowCaseState
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.rememberNavHostEngine
import dagger.hilt.android.AndroidEntryPoint
import dev.keego.volume.booster.screens.NavGraphs
import dev.keego.volume.booster.screens.home.equalizer.EqualizerViewModel
import dev.keego.volume.booster.services.messages.QueryReplyPing
import dev.keego.volume.booster.services.messages.ServiceQueryPing
import dev.keego.volume.booster.services.volumeboost.GlobalVars
import dev.keego.volume.booster.ui.theme.VolumeBoosterTheme
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import timber.log.Timber

val LocalIntroShowCase =
    staticCompositionLocalOf<IntroShowCaseState> { error("No IntroShowCaseState provided") }

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requirePermission()
        ensureNotificationAccess()
        requireRecordAudio(this)
        setContent {
//            val requestLauncher = registerForActivityResult()
            VolumeBoosterTheme {
                val introState = rememberIntroShowCaseState()
                val engine = rememberNavHostEngine()
                val navController = engine.rememberNavController().apply {
                    addOnDestinationChangedListener { _, destination, _ ->
                        Timber.v("navigate -> ${destination.route}")
//                        InternetRequiredHelper.required(this@MainActivity)
                    }
                }
                CompositionLocalProvider(LocalIntroShowCase provides introState) {
                    Scaffold {
                        DestinationsNavHost(
                            modifier = Modifier.padding(it),
                            navGraph = NavGraphs.root,
                            engine = engine,
                            navController = navController,
                            dependenciesContainerBuilder = { // this: DependenciesContainerBuilder<*>
                                // 👇 To tie ActivityViewModel to the activity, making it available to all destinations
                                dependency(hiltViewModel<EqualizerViewModel>(this@MainActivity))
                            }
                        )
                    }
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
