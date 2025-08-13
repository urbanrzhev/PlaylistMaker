package com.example.playlistmaker.player.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.common.domain.api.DataBaseInteractor
import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.common.util.TimeFormat
import com.example.playlistmaker.player.domain.api.MediaPlayerInteractor
import com.example.playlistmaker.player.ui.models.PlayerState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MediaPlayerViewModel(
    private val mediaPlayer: MediaPlayerInteractor,
    private val timeFormat: TimeFormat,
    private val databaseInteractor: DataBaseInteractor
) : ViewModel() {
    private lateinit var activeTrack:Track
    private var job: Job? = null
    private var jobSetFavorites: Job? = null
    private val _playerProgressFlow = MutableStateFlow(TIME_DEFAULT)
    val playerProgressFlow = _playerProgressFlow.asStateFlow()
    private var playerState = MutableLiveData<PlayerState>(PlayerState.Default())
    fun observePlayerState(): LiveData<PlayerState> = playerState

    fun initTrack(track: Track) {
        activeTrack = track
        _playerProgressFlow.value = activeTrack.trackTimeNormal
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

    private fun timeProgress() {
        job?.cancel()
        job = viewModelScope.launch {
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
        return timeFormat.getTimeMM_SS(mediaPlayer.currentPosition())
    }

    fun onFavoritesClicked(value: Boolean) {
        jobSetFavorites?.cancel()
        jobSetFavorites = viewModelScope.launch {
            if (value == true) {
                databaseInteractor.deleteFavoriteTrack(activeTrack.trackId)
            } else {
                databaseInteractor.setFavoriteTrack(activeTrack)
            }
        }
        updateFavoriteLists(activeTrack.trackId, !value)
    }

    companion object {
        private const val DELAY = 300L
        private const val TIME_DEFAULT = "00:00"
        private var favoriteList: MutableList<Pair<Int, Boolean>> = mutableListOf()
        private fun updateFavoriteLists(trackId: Int, like: Boolean) {
            var check: Int? = null
            favoriteList.forEachIndexed { i, pair ->
                if (pair.first == trackId) {
                    check = i
                }
            }
            check?.let {
                favoriteList.removeAt(it)
            }
            favoriteList.add(Pair(trackId, like))
        }

        internal fun changesFavoriteTrack(trackId: Int):Boolean? {
            favoriteList.forEach {
                if(it.first == trackId)
                    return it.second
            }
            return null
        }
    }
}