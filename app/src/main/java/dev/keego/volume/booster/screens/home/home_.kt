package dev.keego.volume.booster.screens.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.keego.volume.booster.R
import dev.keego.volume.booster.screens.home.component._visualizer_page
import dev.keego.volume.booster.screens.home.component._volume_page
import dev.keego.volume.booster.shared.ui._general_top_bar

private const val PAGE_NUMBER = 2

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalPagerApi::class,
)
@RootNavGraph(start = true)
@Destination
@Composable
fun home_(navigator: DestinationsNavigator) {
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            _general_top_bar(
                navigation = {
                    IconButton(onClick = {
                        navigator.navigateUp()
                    }) {
                        Icon(imageVector = Icons.Rounded.Menu, contentDescription = "Menu")
                    }
                },
                title = {
                    Text(
                        text = stringResource(id = R.string.volume_booster),
                        style = MaterialTheme.typography.titleMedium,
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
                            contentDescription = "Edge Lighting",
                        )
                    }
                },
            )
        },
    ) {
        /*TODO update here*/
        HorizontalPager(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .padding(top = 60.dp),
            count = PAGE_NUMBER,
//            userScrollEnabled = true,
            state = pagerState,
        ) { page ->
            when (page) {
                0 -> {
                    _volume_page(
                        modifier = Modifier.fillMaxSize(),
                    )
                }

                else -> {
                    _visualizer_page(
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }
        }
    }
}
