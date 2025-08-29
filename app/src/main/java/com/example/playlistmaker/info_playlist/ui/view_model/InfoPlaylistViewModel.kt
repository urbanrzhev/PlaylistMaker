package com.example.playlistmaker.info_playlist.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.common.domain.models.Playlist

class InfoPlaylistViewModel:ViewModel() {
    private val _playlist = MutableLiveData(Playlist())
    val observePlaylist:LiveData<Playlist> = _playlist

    fun getPlaylist(playlist:Playlist){
        _playlist.value = playlist
    }
}