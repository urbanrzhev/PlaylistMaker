package com.example.playlistmaker.info_playlist.ui.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.common.data.db.dao.PlaylistDao
import com.example.playlistmaker.common.domain.api.DataBasePlaylistsInteractor
import com.example.playlistmaker.common.domain.models.Playlist
import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.common.util.OrthographyCount
import com.example.playlistmaker.common.util.TimeFormat
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class InfoPlaylistViewModel(
    private val databasePlaylistInteractor: DataBasePlaylistsInteractor,
    private val orthographyCount: OrthographyCount,
    private val timeFormat: TimeFormat
):ViewModel() {
    private var getTracksJob:Job? = null
    private var deleteTrackJob:Job? = null
    private val _playlist = MutableLiveData(Playlist())
    val observePlaylist:LiveData<Playlist> = _playlist
    private val _total_time = MutableLiveData("")
    val observeTotalTime:LiveData<String> = _total_time
    private val _total_tracks = MutableLiveData("")
    val observeTotalTracks:LiveData<String> = _total_tracks
    private val _tracks = MutableLiveData(listOf<Track>())
    val observeTracks:LiveData<List<Track>> = _tracks

    fun createPlaylist(playlist:Playlist){
        _playlist.value = playlist
        getTracksJob?.cancel()
        updateTracks(playlist.name)
    }

    fun deleteTrack(track:Track){
        deleteTrackJob?.cancel()
        deleteTrackJob = viewModelScope.launch {
            databasePlaylistInteractor.deleteTrackFromPlaylist(track, _playlist.value?.name ?:"")
            createPlaylist(_playlist.value!!)
        }
    }

    private fun updateTracks(playlistName:String){
        getTracksJob = viewModelScope.launch {
            databasePlaylistInteractor.getTracks(playlistName = playlistName).collect{ tracks->
                _tracks.postValue(tracks)
                totalTimeOrthography(tracks)
            }
        }
    }

    private fun totalTimeOrthography(tracks:List<Track>){
        var totalTime = 0
        tracks.forEach {
            try {
                totalTime += it.trackTimeMillis.toInt()
            }catch (_:NumberFormatException){}
        }
        val timeFormatTotalTime = timeFormat.getTimeMM(totalTime).toInt()
        _total_time.value = orthographyCount.orthographyCountMinutes(timeFormatTotalTime)
        totalTracksOrthography(tracks)
    }

    private fun totalTracksOrthography(tracks: List<Track>){
        if(tracks.isNotEmpty()) {
            val totalTracks = orthographyCount.orthographyCountTracks(tracks.size)
            _total_tracks.value = totalTracks
        }else
            _total_tracks.value = ""
    }

    fun getSizeTrackList():Int = _tracks.value?.size?:0
}