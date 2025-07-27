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
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.getKoin

class MediaPlayerViewModel(
    private val getActiveTrackUseCase: GetActiveTrackUseCase,
    private val mediaPlayer: MediaPlayerInteractor
) : ViewModel() {
    private var job: Job? = null
    private var playerState = MutableLiveData<PlayerState>(PlayerState.Default())
    private val activeTrack = getActiveTrackUseCase.execute()
    fun observePlayerState(): LiveData<PlayerState> = playerState

    init {
        val url = activeTrack.previewUrl
        if (url.isNotEmpty()) {
            mediaPlayer.prepare(url, consumerPrepared = {
                playerState.value = PlayerState.Prepared()
            }, consumerCompleted = {
                playerState.value = PlayerState.Prepared()
            })
        }
    }

    fun getActiveTrack(): Track {
        return activeTrack
    }

    private fun timeProgress() {
        job = viewModelScope.launch {
            while (mediaPlayer.isPlaying()) {
                playerState.value = PlayerState.Playing(getCurrentPosition())
                delay(DELAY)
            }
        }
    }

    fun control() {
        mediaPlayer.control(start = {
            playerState.value = PlayerState.Playing(progress = getCurrentPosition())
            timeProgress()
        }, pause = {
            playerState.value = PlayerState.Paused(progress = getCurrentPosition())
        })
    }

    fun pause() {
        mediaPlayer.pause()
        playerState.value = PlayerState.Paused(getCurrentPosition())
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
    }
}