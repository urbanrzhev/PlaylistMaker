package com.example.playlistmaker.new_playlist.ui.view_model

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
import com.example.playlistmaker.common.domain.api.DataBasePlaylistsInteractor
import com.example.playlistmaker.common.domain.models.Playlist
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

open class NewPlaylistViewModel(
    private val context:Context,
    private val database:DataBasePlaylistsInteractor
):ViewModel() {
    private var createJob: Job? = null
    private val _buttonCreateEnable = MutableLiveData(false)
    internal val observeButtonCreateEnable:LiveData<Boolean> = _buttonCreateEnable
    internal val _closeFragment = MutableLiveData(false)
    internal val observeCloseFragment:LiveData<Boolean> = _closeFragment
    private val _enablePressedCallback = MutableLiveData(false)
    internal val observeEnablePressedCallback:LiveData<Boolean> = _enablePressedCallback
    internal var _playlist = Playlist()
    private val _photo = MutableLiveData("")
    internal val observePhoto:LiveData<String> = _photo

    fun setPhoto(uri:String){
        _playlist.apply {
            photo = uri
        }
        _photo.value = uri
        updateStatePlaylist()
    }

    fun setName(name:String){
        _playlist.apply {
            this.name = name
        }
        updateStatePlaylist()
    }

    fun setDescription(description:String){
        _playlist.apply {
            this.description = description
        }
        updateStatePlaylist()
    }

    private fun updateStatePlaylist(){
        if(_playlist.photo.isNotEmpty() || _playlist.name.isNotEmpty() || _playlist.description.isNotEmpty()) {
            _enablePressedCallback.value = true
        }else{
            _enablePressedCallback.value = false
        }
        if(_playlist.name.isNotEmpty())
            _buttonCreateEnable.value = true
        else
            _buttonCreateEnable.value = false
    }

    fun createPlaylist(){
        createJob?.cancel()
        createJob = viewModelScope.launch {
            _playlist.let {
                database.setPlaylist(it)
                createdMessage(it)
                _enablePressedCallback.postValue(false)
                _closeFragment.postValue(true)
            }
        }
    }

    fun getPlaylist():Playlist = _playlist

    fun getBackPressedCallback() = _enablePressedCallback.value

    private fun createdMessage(playlist:Playlist){
        Toast.makeText(context,
            "${context.getString(R.string.create_one)} " +
                    "${playlist.name} ${context.getString(R.string.create_two)}",Toast.LENGTH_SHORT)
            .show()
    }
}