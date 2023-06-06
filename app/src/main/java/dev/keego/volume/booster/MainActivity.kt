package dev.keego.volume.booster

import android.content.Intent
import android.os.Bundle
import android.provider.Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dagger.hilt.android.AndroidEntryPoint
import dev.keego.volume.booster.ui.theme.VolumeBoosterTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requirePermission()
        setContent {
            VolumeBoosterTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
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
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    VolumeBoosterTheme {
        Greeting("Android")
    }
}