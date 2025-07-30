package com.example.playlistmaker.player.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.common.util.TimeFormat
import com.example.playlistmaker.player.domain.api.GetActiveTrackUseCase
import com.example.playlistmaker.player.domain.api.MediaPlayerInteractor
import com.example.playlistmaker.player.ui.models.PlayerState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.getKoin

class MediaPlayerViewModel(
    private val getActiveTrackUseCase: GetActiveTrackUseCase,
    private val mediaPlayer: MediaPlayerInteractor
) : ViewModel() {
    private val activeTrack = getActiveTrackUseCase.execute()
    private val _playerProgressFlow = MutableStateFlow(activeTrack.trackTimeNormal)
    val playerProgressFlow = _playerProgressFlow.asStateFlow()
    private var playerState = MutableLiveData<PlayerState>(PlayerState.Default())
    fun observePlayerState(): LiveData<PlayerState> = playerState

    init {
        val url = activeTrack.previewUrl
        if (url.isNotEmpty()) {
            mediaPlayer.prepare(url, consumerPrepared = {
                playerState.value = PlayerState.Prepared()
            }, consumerCompleted = {
                _playerProgressFlow.value = TIME_DEFAULT
                playerState.value = PlayerState.Prepared()
            })
        }
    }

    fun getActiveTrack(): Track {
        return activeTrack
    }

    private fun timeProgress() {
        viewModelScope.launch {
            while (mediaPlayer.isPlaying()) {
                _playerProgressFlow.value = getCurrentPosition()
                delay(DELAY)
            }
        }
    }

    fun control() {
        mediaPlayer.control(start = {
            playerState.value = PlayerState.Playing()
            timeProgress()
        }, pause = {
            playerState.value = PlayerState.Paused()
        })
    }

    fun pause() {
        mediaPlayer.pause()
        playerState.value = PlayerState.Paused()
    }

    override fun onCleared() {
        mediaPlayer.release()
    }

    private fun getCurrentPosition(): String {
        return getKoin().get<TimeFormat> {
            parametersOf(mediaPlayer.currentPosition())
        }.getTimeMM_SS()
    }

    companion object {
        private const val DELAY = 300L
        private const val TIME_DEFAULT = "00:00"
    }
}