package dev.keego.volume.booster.screens.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.canopas.lib.showcase.IntroShowCaseScaffold
import com.canopas.lib.showcase.ShowcaseStyle
import com.canopas.lib.showcase.introShowCaseTarget
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.keego.volume.booster.LocalIntroShowCase
import dev.keego.volume.booster.R
import dev.keego.volume.booster.screens.destinations.language_Destination
import dev.keego.volume.booster.screens.home.component._drawer_content
import dev.keego.volume.booster.screens.home.component._home_playback
import dev.keego.volume.booster.screens.home.equalizer.EqualizerViewModel
import dev.keego.volume.booster.screens.home.equalizer._equalizer_page
import dev.keego.volume.booster.screens.home.volume._volume_page
import dev.keego.volume.booster.section.model.PlaybackCommand
import dev.keego.volume.booster.setup.preference.AppPreferences
import dev.keego.volume.booster.shared.ui._general_top_bar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val PAGE_NUMBER = 2

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)
@RootNavGraph(start = true)
@Destination
@Composable
fun home_(
    navigator: DestinationsNavigator,
    equalizerViewModel: EqualizerViewModel,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val enabledEqualizer by homeViewModel.enabledEqualizer.collectAsStateWithLifecycle()
    val enabledVibration by homeViewModel.enabledVibration.collectAsStateWithLifecycle()

    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val showcaseState = LocalIntroShowCase.current

    val putPlaybackCommand = remember<(PlaybackCommand) -> Unit> {
        { homeViewModel.putPlaybackCommand(it) }
    }

    var isShowIntroShowCase by remember {
        mutableStateOf(AppPreferences.isShowedTargetGuide == false)
    }

    val screenWidth = LocalConfiguration.current.screenWidthDp
    val drawerWidth = screenWidth * 4f / 5f
    IntroShowCaseScaffold(
        showIntroShowCase = isShowIntroShowCase,
        onShowCaseCompleted = {
            isShowIntroShowCase = false
//            AppPreferences.isShowedTargetGuide = true
            scope.launch {
                drawerState.apply {
                    open()
                    delay(1500)
                    if (isOpen) close()
                }
            }
        },
        state = showcaseState
    ) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(
                    modifier = Modifier.width(drawerWidth.dp)
                ) {
                    /* Drawer content */
                    _drawer_content(
                        vibrationValue = enabledVibration,
                        equalizerValue = enabledEqualizer,
                        onVibrationValueChange = homeViewModel::toggleEnabledVibration,
                        onEqualizerValueChange = homeViewModel::toggleEnabledEqualizer,
                        onLanguageClick = {
                            navigator.navigate(language_Destination)
                        }
                    )
                }
            }
        ) {
            Scaffold(
                topBar = {
                    _general_top_bar(
                        navigation = {
                            IconButton(onClick = {
                                scope.launch {
                                    drawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                            }) {
                                Icon(imageVector = Icons.Rounded.Menu, contentDescription = "Menu")
                            }
                        },
                        title = {
                            Text(
                                text = stringResource(id = R.string.volume_booster),
                                style = MaterialTheme.typography.titleMedium
                            )
                        },
                        actions = {
                            IconButton(onClick = {
                                /*TODO update here*/
                            }) {
                                Icon(
                                    imageVector = Icons.Rounded.Clear,
                                    contentDescription = "Theme"
                                )
                            }
                            IconButton(
                                onClick = {
                                    /*TODO update here*/
                                },
                                modifier = Modifier.introShowCaseTarget(
                                    state = showcaseState,
                                    index = 0,
                                    style = ShowcaseStyle.Default.copy(
                                        // specify color of background
                                        backgroundColor = MaterialTheme.colorScheme.primary,
                                        // specify transparency of background
                                        backgroundAlpha = 0.98f,
                                        // specify color of target circle
                                        targetCircleColor = Color.White
                                    ),
                                    content = {
                                        Column {
                                            Text(
                                                text = stringResource(id = R.string.edge_lighting),
                                                style = MaterialTheme.typography.titleMedium.copy(
                                                    color = MaterialTheme.colorScheme.onBackground
                                                )
                                            )
                                            Text(
                                                text = stringResource(
                                                    id = R.string.click_here_to_open_edge_lighting
                                                ),
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    color = MaterialTheme.colorScheme.onBackground
                                                )
                                            )
                                        }
                                    }
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Phone,
                                    contentDescription = "Edge Lighting"
                                )
                            }
                        }
                    )
                }
            ) {
                Column(modifier = Modifier.fillMaxSize().padding(it)) {
                    /*TODO update here*/
                    // page items
                    Row(modifier = Modifier) {
                        Text(
                            text = "Volume",
                            modifier = Modifier.clickable {
                                scope.launch {
                                    pagerState.animateScrollToPage(0)
                                }
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Visualizer",
                            modifier = Modifier.clickable {
                                scope.launch {
                                    pagerState.animateScrollToPage(1)
                                }
                            }
                        )
                    }
                    HorizontalPager(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(top = 12.dp),
                        pageCount = PAGE_NUMBER,
                        userScrollEnabled = true,
                        state = pagerState
                    ) { page ->
                        when (page) {
                            0 -> {
                                _volume_page(
                                    modifier = Modifier.fillMaxSize()
                                )
                            }

                            else -> {
                                _equalizer_page(
                                    modifier = Modifier.fillMaxSize(),
                                    navigator = navigator,
                                    viewModel = equalizerViewModel
                                )
                            }
                        }
                    }
                    _home_playback(
                        onContentClick = {
                            putPlaybackCommand.invoke(PlaybackCommand.ContentClick)
                        },
                        onPlay = { putPlaybackCommand.invoke(PlaybackCommand.Play) },
                        onPause = { putPlaybackCommand.invoke(PlaybackCommand.Pause) },
                        onPrevious = { putPlaybackCommand.invoke(PlaybackCommand.Previous) },
                        onNext = { putPlaybackCommand.invoke(PlaybackCommand.Next) }
                    )
                }
            }
        }
    }
}
