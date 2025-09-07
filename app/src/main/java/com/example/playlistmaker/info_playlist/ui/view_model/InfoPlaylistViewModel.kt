package com.example.playlistmaker.info_playlist.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.common.domain.api.DataBasePlaylistsInteractor
import com.example.playlistmaker.common.domain.models.Playlist
import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.common.util.OrthographyCount
import com.example.playlistmaker.common.util.TimeFormat
import com.example.playlistmaker.info_playlist.domain.api.InfoSharingUseCase
import com.example.playlistmaker.info_playlist.ui.models.InfoTracksState
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class InfoPlaylistViewModel(
    private val databasePlaylistInteractor: DataBasePlaylistsInteractor,
    private val orthographyCount: OrthographyCount,
    private val timeFormat: TimeFormat,
    private val sharingUseCase: InfoSharingUseCase
) : ViewModel() {
    private var getTracksJob: Job? = null
    private var deleteTrackJob: Job? = null
    private var createPlaylistJob: Job? = null
    private var deletePlaylistFirstStageJob: Job? = null
    private val _recyclerState = MutableLiveData<InfoTracksState>(InfoTracksState.Idle)
    internal val observeRecyclerState: LiveData<InfoTracksState> = _recyclerState
    private val _menuBehaviorState =
        MutableLiveData(BottomSheetBehavior.STATE_HIDDEN)
    val observeMenuBehaviorState: LiveData<Int> = _menuBehaviorState
    private val _close = MutableLiveData(false)
    val observeClose: LiveData<Boolean> = _close
    private val _playlist = MutableLiveData(Playlist())
    val observePlaylist: LiveData<Playlist> = _playlist
    private val _total_time = MutableLiveData("")
    val observeTotalTime: LiveData<String> = _total_time
    private val _total_tracks = MutableLiveData("")
    val observeTotalTracks: LiveData<String> = _total_tracks
    private val _tracks = MutableLiveData(listOf<Track>())
    val observeTracks: LiveData<List<Track>> = _tracks

    fun createPlaylist(playlistId: Long) {
        createPlaylistJob?.cancel()
        createPlaylistJob = viewModelScope.launch {
            databasePlaylistInteractor.getPlaylist(playlistId).collect { playlist ->
                _playlist.value = playlist
            }
        }
        updateTracks(playlistId)
    }

    fun deleteTrack(track: Track) {
        deleteTrackJob?.cancel()
        deleteTrackJob = viewModelScope.launch {
            databasePlaylistInteractor.deleteTrackFromPlaylist(
                track,
                _playlist.value?.playlistId ?: 0
            )
            updateTracks(_playlist.value?.playlistId!!)
        }
    }

    fun executeSharing() {
        sharingUseCase.execute(_playlist.value!!, _tracks.value!!)
    }

    fun deletePlaylist() {
        var deleteListForSecondStage: List<Int>? = null
        deletePlaylistFirstStageJob?.cancel()
        deletePlaylistFirstStageJob = viewModelScope.launch {
            databasePlaylistInteractor.deletePlaylistFirstStage(_playlist.value!!).collect { list ->
                if (list.isNotEmpty())
                    deleteListForSecondStage = list
            }
        }
        deletePlaylistFirstStageJob?.invokeOnCompletion {
            CoroutineScope(Dispatchers.IO).launch {
                deleteListForSecondStage?.let {
                    databasePlaylistInteractor.deletePlaylistSecondStage(it)
                }
            }
            _close.postValue(true)
        }
    }

    fun setMenuBehaviorState(state: Int) {
        _menuBehaviorState.value = state
    }

    private fun updateTracks(playlistId: Long) {
        getTracksJob?.cancel()
        getTracksJob = viewModelScope.launch {
            databasePlaylistInteractor.getTracks(playlistId = playlistId).collect { tracks ->
                _tracks.postValue(tracks)
                totalTimeOrthography(tracks)
                if (tracks.isNotEmpty())
                    _recyclerState.postValue(InfoTracksState.Tracks)
                else
                    _recyclerState.postValue(InfoTracksState.NotTracks)
            }
        }
    }

    private fun totalTimeOrthography(tracks: List<Track>) {
        var totalTime = 0
        tracks.forEach {
            try {
                totalTime += it.trackTimeMillis.toInt()
            } catch (_: NumberFormatException) {
            }
        }
        val timeFormatTotalTime = timeFormat.getTimeMM(totalTime).toInt()
        _total_time.value = orthographyCount.orthographyCountMinutes(timeFormatTotalTime)
        totalTracksOrthography(tracks)
    }

    private fun totalTracksOrthography(tracks: List<Track>) {
        val totalTracks = orthographyCount.orthographyCountTracks(tracks.size)
        _total_tracks.value = totalTracks
    }

    fun getSizeTrackList(): Int = _tracks.value?.size ?: 0
}