package com.example.playlistmaker.media_library.ui.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.common.domain.api.DataBasePlaylistsInteractor
import com.example.playlistmaker.common.domain.models.Playlist
import com.example.playlistmaker.media_library.ui.models.RecyclerState
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    private val database:DataBasePlaylistsInteractor
): ViewModel(){
    private val _playlistsState = MutableLiveData<RecyclerState<List<Playlist>>>(RecyclerState.Idle())
    val playlistState:LiveData<RecyclerState<List<Playlist>>> = _playlistsState

    fun loadDatabase(){
        viewModelScope.launch {
            database.getPlaylists().collect { list->
                when{
                    list.isNotEmpty() -> _playlistsState.postValue(RecyclerState.Success(list))
                }
            }
        }
    }
}