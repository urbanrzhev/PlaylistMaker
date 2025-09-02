package com.example.playlistmaker.modify_playlist.ui.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.common.domain.api.DataBasePlaylistsInteractor
import com.example.playlistmaker.common.domain.models.Playlist
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ModifyPlaylistViewModel(
    private val databasePlaylistInteractor: DataBasePlaylistsInteractor,
):ViewModel() {
    private var createPlaylistJob: Job? = null
    private var saveModifyPlaylistJob: Job? = null
    private lateinit var legacyName:String
    private val _buttonSaveEnabled = MutableLiveData(true)
    val observeButtonSaveEnabled:LiveData<Boolean> = _buttonSaveEnabled
    private val _close = MutableLiveData(false)
    val observeClose:LiveData<Boolean> = _close
    private val _playlist = MutableLiveData<Playlist>()
    val observePlaylist:LiveData<Playlist> = _playlist
    fun setPlaylist(playlistName: String){
        legacyName = playlistName
        createPlaylistJob?.cancel()
        createPlaylistJob = viewModelScope.launch {
            databasePlaylistInteractor.getPlaylist(playlistName).collect{ playlist ->
                _playlist.value = playlist
            }
        }
    }

    fun setName(name:String){
        if(name.isEmpty())
            _buttonSaveEnabled.value = false
        else{
            _buttonSaveEnabled.value = true
            _playlist.value?.name = name
        }
    }

    fun setDescription(description:String){
        _playlist.value?.description = description
    }

    fun saveModifyPlaylist(){
        saveModifyPlaylistJob?.cancel()
        saveModifyPlaylistJob = viewModelScope.launch {
            databasePlaylistInteractor.setPlaylist(_playlist.value!!)
            _close.postValue(true)
        }
    }
}