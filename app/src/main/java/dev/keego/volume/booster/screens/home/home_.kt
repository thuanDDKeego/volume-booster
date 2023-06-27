package dev.keego.volume.booster.screens.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.keego.volume.booster.R
import dev.keego.volume.booster.screens.home.equalizer._equalizer_page
import dev.keego.volume.booster.screens.home.volume._volume_page
import dev.keego.volume.booster.shared.ui._general_top_bar
import kotlinx.coroutines.launch

private const val PAGE_NUMBER = 2

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)
@RootNavGraph(start = true)
@Destination
@Composable
fun home_(navigator: DestinationsNavigator) {
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet { /* Drawer content */ }
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
                            Icon(imageVector = Icons.Rounded.Clear, contentDescription = "Theme")
                        }
                        IconButton(onClick = {
                            /*TODO update here*/
                        }) {
                            Icon(
                                imageVector = Icons.Rounded.Phone,
                                contentDescription = "Edge Lighting"
                            )
                        }
                    }
                )
            }
        ) {
            Box(modifier = Modifier.fillMaxSize().padding(it)) {
                /*TODO update here*/
                HorizontalPager(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 60.dp),
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
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
                // page items
                Row(modifier = Modifier.align(Alignment.TopCenter).padding(top = 12.dp)) {
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
            }
        }
    }
}
