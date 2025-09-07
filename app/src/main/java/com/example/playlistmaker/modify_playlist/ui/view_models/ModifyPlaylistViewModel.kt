package com.example.playlistmaker.modify_playlist.ui.view_models

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.common.domain.api.DataBasePlaylistsInteractor
import com.example.playlistmaker.common.domain.models.Playlist
import com.example.playlistmaker.new_playlist.domain.api.FileUseCase
import com.example.playlistmaker.new_playlist.ui.view_model.NewPlaylistViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ModifyPlaylistViewModel(
    private val context: Context,
    private val fileUseCase: FileUseCase,
    private val databasePlaylistInteractor: DataBasePlaylistsInteractor,
) : NewPlaylistViewModel(context,fileUseCase, databasePlaylistInteractor) {
    private var setPlaylistJob: Job? = null
    private var updatePlaylistJob: Job? = null
    private val _showPlaylist = MutableLiveData<Playlist>(_playlist)
    val observeShowPlaylist: LiveData<Playlist> = _showPlaylist
    fun setPlaylist(playlistId: Long) {
        setPlaylistJob?.cancel()
        setPlaylistJob = viewModelScope.launch {
            databasePlaylistInteractor.getPlaylist(playlistId).collect { playlist ->
                _playlist = playlist
                setPhoto(playlist.photo)
                setName(playlist.name)
                setDescription(playlist.description)
                _showPlaylist.postValue(playlist)
            }
        }
    }

    fun updatePlaylist() {
        updatePlaylistJob?.cancel()
        updatePlaylistJob = viewModelScope.launch {
            databasePlaylistInteractor.updatePlaylist(_playlist)
            _closeFragment.postValue(true)
        }
    }
}