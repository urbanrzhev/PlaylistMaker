package com.example.playlistmaker.player.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.common.domain.api.DataBasePlaylistsInteractor
import com.example.playlistmaker.common.domain.api.DataBaseFavoritesTracksInteractor
import com.example.playlistmaker.common.domain.models.Playlist
import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.common.util.SingleLiveEvent
import com.example.playlistmaker.common.util.TimeFormat
import com.example.playlistmaker.player.domain.api.MediaPlayerInteractor
import com.example.playlistmaker.player.ui.models.PlayerState
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MediaPlayerViewModel(
    private val mediaPlayer: MediaPlayerInteractor,
    private val timeFormat: TimeFormat,
    private val databaseFavoritesTracksInteractor: DataBaseFavoritesTracksInteractor,
    private val databasePlaylistsInteractor: DataBasePlaylistsInteractor
) : ViewModel() {
    private lateinit var activeTrack: Track
    private val _showMessage = SingleLiveEvent<Pair<Boolean, String>>()
    val observeShowMessage: LiveData<Pair<Boolean, String>> = _showMessage
    private val _bottomSheetBehaviorState = MutableLiveData(BottomSheetBehavior.STATE_HIDDEN)
    val observeBottomSheetBehaviorState: LiveData<Int> = _bottomSheetBehaviorState
    private val _itemsAdapter = MutableLiveData(listOf<Playlist>())
    val observeItemsAdapter: LiveData<List<Playlist>> = _itemsAdapter
    private var job: Job? = null
    private var addTrackInPlaylistJob: Job? = null
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
                databaseFavoritesTracksInteractor.deleteFavoriteTrack(activeTrack.trackId)
            } else {
                databaseFavoritesTracksInteractor.setFavoriteTrack(activeTrack)
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
        if (state != BottomSheetBehavior.STATE_SETTLING && state != BottomSheetBehavior.STATE_DRAGGING)
            _bottomSheetBehaviorState.value = state
    }

    fun addTrackInPlaylist(playlist: Playlist) {
        playlist.idsTrack.forEach {
            if (it == activeTrack.trackId) {
                _showMessage.value = Pair(false, playlist.name)
                return
            }
        }
        addTrackInPlaylistJob?.cancel()
        addTrackInPlaylistJob = viewModelScope.launch {
            databasePlaylistsInteractor.addTrackInPlaylist(
                track = activeTrack,
                playlistName = playlist.name
            ).collect {
                if (it) {
                    _showMessage.value = Pair(true, playlist.name)
                    stateBottomSheetBehavior(BottomSheetBehavior.STATE_HIDDEN)
                    playlist.idsTrack.add(activeTrack.trackId)
                }
            }
        }
    }

    companion object {
        private const val DELAY = 300L
        private const val TIME_DEFAULT = "00:00"
    }
}