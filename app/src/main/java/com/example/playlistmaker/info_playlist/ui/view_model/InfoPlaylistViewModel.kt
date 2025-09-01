package com.example.playlistmaker.info_playlist.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.common.data.db.dao.PlaylistDao
import com.example.playlistmaker.common.domain.api.DataBasePlaylistsInteractor
import com.example.playlistmaker.common.domain.models.Playlist
import kotlinx.coroutines.launch

class InfoPlaylistViewModel(
    private val databasePlaylistInteractor: DataBasePlaylistsInteractor
):ViewModel() {
    private val _playlist = MutableLiveData(Playlist())
    val observePlaylist:LiveData<Playlist> = _playlist
    private val _tracks = MutableLiveData(listOf<Int>())
    val observeTracks:LiveData<List<Int>> = _tracks

    fun setPlaylist(playlist:Playlist){
        _playlist.value = playlist
    }

    fun getTracks(){
        viewModelScope.launch {
            databasePlaylistInteractor.getTrackListInPlaylist()
            _tracks.postValue(databasePlaylist.getItemsId())
        }
    }
}