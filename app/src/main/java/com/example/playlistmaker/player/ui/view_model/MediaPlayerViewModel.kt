package com.example.playlistmaker.player.ui.view_model

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
import com.example.playlistmaker.common.domain.api.DataBasePlaylistsInteractor
import com.example.playlistmaker.common.domain.api.DataBaseTracksInteractor
import com.example.playlistmaker.common.domain.models.Playlist
import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.common.util.TimeFormat
import com.example.playlistmaker.player.domain.api.MediaPlayerInteractor
import com.example.playlistmaker.player.ui.models.PlayerState
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MediaPlayerViewModel(
    private val mediaPlayer: MediaPlayerInteractor,
    private val timeFormat: TimeFormat,
    private val databaseTracksInteractor: DataBaseTracksInteractor,
    private val databasePlaylistsInteractor: DataBasePlaylistsInteractor,
    private val context: Context
) : ViewModel() {
    private lateinit var activeTrack: Track
    private val _overlay = MutableLiveData(false)
    val observeOverlay: LiveData<Boolean> = _overlay
    private val _bottomSheetBehaviorState = MutableLiveData(BottomSheetBehavior.STATE_HIDDEN)
    val observeBottomSheetBehaviorState: LiveData<Int> = _bottomSheetBehaviorState
    private val _itemsAdapter = MutableLiveData(listOf<Playlist>())
    val observeItemsAdapter: LiveData<List<Playlist>> = _itemsAdapter
    private var job: Job? = null
    private var updatePlaylistJob: Job? = null
    private var jobSetFavorites: Job? = null
    private val _playerProgressFlow = MutableStateFlow(TIME_DEFAULT)
    val playerProgressFlow = _playerProgressFlow.asStateFlow()
    private var playerState = MutableLiveData<PlayerState>(PlayerState.Default())
    fun observePlayerState(): LiveData<PlayerState> = playerState

    fun initTrack(track: Track) {
        activeTrack = track
        _playerProgressFlow.value = activeTrack.trackTimeNormal
        val url = activeTrack.previewUrl
        if (url.isNotEmpty() && playerState.value != PlayerState.Prepared()) {
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
                databaseTracksInteractor.deleteFavoriteTrack(activeTrack.trackId)
            } else {
                databaseTracksInteractor.setFavoriteTrack(activeTrack)
            }
        }
    }

    fun getPlaylists() {
        viewModelScope.launch {
            databasePlaylistsInteractor.getPlaylists()
                .collect {
                    _itemsAdapter.postValue(it)
                }
        }
    }

    fun stateBottomSheetBehavior(state: Int) {
        _bottomSheetBehaviorState.value = state
    }

    fun updatePlaylist(playlist: Playlist) {
        updatePlaylistJob?.cancel()
        updatePlaylistJob = viewModelScope.launch {
            if (databasePlaylistsInteractor.updatePlaylist(playlist, activeTrack.trackId))
                showToast("${context.getString(R.string.success_add_playlist)} ${playlist.name}")
            else
                showToast("${context.getString(R.string.before_add_playlist)} ${playlist.name}")
            stateBottomSheetBehavior(BottomSheetBehavior.STATE_HIDDEN)
        }
    }

    private suspend fun showToast(message: String) {
        withContext(Dispatchers.Main) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    fun overlayVisible(value: Boolean) {
        _overlay.value = value
    }

    companion object {
        private const val DELAY = 300L
        private const val TIME_DEFAULT = "00:00"
    }
}