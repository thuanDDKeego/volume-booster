package dev.keego.volume.booster.screens.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.keego.volume.booster.model.Command
import dev.keego.volume.booster.repositories.BoostServiceRepository
import dev.keego.volume.booster.repositories.NotificationPlaybackRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val notificationPlaybackRepository: NotificationPlaybackRepository,
    private val boostServiceRepository: BoostServiceRepository,
) :
    ViewModel() {
    data class PlayBackState(
        val name: String = "No current playing",
        val isPlaying: Boolean = false,
    )

    data class BoostVolumeState(
        val enable: Boolean = false,
        val db: Int = 0,
    )

    private val _playbackState = MutableStateFlow(PlayBackState())
    val playbackState = _playbackState.asStateFlow()

    private val _boostState = MutableStateFlow(BoostVolumeState())
    val boostState = _boostState.asStateFlow()

    init {
        viewModelScope.launch {
            notificationPlaybackRepository.apply {
                launch {
                    playback.collectLatest {
                        _playbackState.value = _playbackState.value.copy(
                            name = it.name,
                            isPlaying = it.isPlaying,
                        )
                    }
                }
            }
            launch {
                boostServiceRepository.db.collectLatest {
                    _boostState.value = _boostState.value.copy(db = it)
                }
            }
            launch {
                boostServiceRepository.enable.collectLatest {
                    _boostState.value = _boostState.value.copy(enable = it)
                }
            }
        }
    }

//    fun setupBoostService(context: Context) {
//        viewModelScope.launch {
//            _boostState.collectLatest {
//                if (it.enable) {
//                    ServiceDispatcher.startService(context)
//                } else {
//                    ServiceDispatcher.stopService(context)
//                }
//                it.db.let {
//                    try {
//                        EventBus.getDefault().post(ServiceCommand.UPDATE)
//                    } catch (ignored: NumberFormatException) {
//                        Toast.makeText(context, "Invalid loudness number", Toast.LENGTH_SHORT)
//                            .show()
//                    }
//                }
//            }
//        }
//    }

    fun putCommand(command: Command) {
        notificationPlaybackRepository.putCommand(command)
    }

    fun toggleEnableBoost(context: Context, value: Boolean) {
        boostServiceRepository.toggleEnableService(context, value)
    }

    fun updateBoostValue(value: Float) {
        Timber.d("updateBoostValue $value")
        boostServiceRepository.updateDb(value.toInt())
    }
}
