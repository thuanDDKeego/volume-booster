package dev.keego.volume.booster.screens.language

import androidx.activity.compose.BackHandler
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import coil.compose.rememberAsyncImagePainter
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.keego.volume.booster.R
import dev.keego.volume.booster.shared.extensions.mirrorable
import dev.keego.volume.booster.shared.utils.LanguageUtils
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun language_(
    navigator: DestinationsNavigator
) {
    val context = LocalContext.current
    var languageSelected by remember {
        mutableStateOf(LanguageUtils.getCurrent())
    }
    BackHandler(true) {
//        Haki.placement("language_CLICK_BackHandler").tracking().interstitial(ShowMode.IMMEDIATELY)
//            .show(context as Activity) {
//                finishActivity()
//            }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = Color.Black,
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = {
//                        Haki.placement("language_CLICK_back").tracking()
//                            .interstitial(ShowMode.IMMEDIATELY).show(context as Activity) {
//                                finishActivity()
//                            }
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "BACK",
                            modifier = Modifier
                                .size(24.dp)
                                .mirrorable(context)
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                title = {
                    Text(
                        text = stringResource(id = R.string.language),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                actions = {
                    IconButton(onClick = {
                        LanguageUtils.updateCurrent(languageSelected)
                        // Update the app locale immediately
                        AppCompatDelegate.setApplicationLocales(
                            LocaleListCompat.forLanguageTags(
                                languageSelected?.locale
                            )
                        )
                        navigator.navigateUp()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Check",
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                LanguageUtils.getAll().forEach { language ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 5.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (languageSelected?.name == language.name) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.surface
                                }
                            )
                            .clickable {
//                                Firebase.analytics.logEvent("language_CLICK_country") {
//                                    param("language_CLICK_country", language.name.toFirebaseName())
//                                }
                                languageSelected = language
                            }
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(language.flagImg),
                                contentDescription = "national flag",
                                modifier = Modifier
                                    .size(height = 24.dp, width = 36.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                contentScale = ContentScale.Crop
                            )

                            Text(
                                text = language.nameLocale,
                                style = MaterialTheme.typography.titleSmall.copy(
                                    color = MaterialTheme.colorScheme.onBackground
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 24.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
