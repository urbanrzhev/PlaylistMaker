package com.example.playlistmaker.new_playlist.ui.view_model

import android.content.Context
import android.util.Log
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

class NewPlaylistViewModel(
    private val context:Context,
    private val database:DataBasePlaylistsInteractor
):ViewModel() {
    private var createJob: Job? = null
    private val _buttonCreateEnable = MutableLiveData(false)
    val observeButtonCreateEnable:LiveData<Boolean> = _buttonCreateEnable
    private val _closeFragment = MutableLiveData(false)
    val observeCloseFragment:LiveData<Boolean> = _closeFragment
    private val _enablePressedCallback = MutableLiveData(false)
    val observeEnablePressedCallback:LiveData<Boolean> = _enablePressedCallback
    //private val _playlist = MutableLiveData(Playlist())
    private val _playlist = Playlist()
    private val _photo = MutableLiveData("")
    val observePhoto:LiveData<String> = _photo
    //val observePlaylist:LiveData<Playlist> = _playlist

    fun setPhoto(uri:String){
        Log.v("my","Observe setPhoto")
        _playlist = _playlist.value?.copy(photo = uri)
        _photo.value = uri
        updateStatePlaylist()
       // _navigateUp.value = true
    }

    fun setName(name:String){
        Log.v("my","Observe setName")
        _playlist.value = _playlist.value?.copy(name = name)
        updateStatePlaylist()
      //  _navigateUp.value = true
    }

    fun setDescription(description:String){
        Log.v("my","Observe setDescription")
        _playlist.value = _playlist.value?.copy(description = description)
        updateStatePlaylist()
    }

    private fun updateStatePlaylist(){
        Log.v("my","vM update playlist ${_playlist.value}")
        if(_playlist.value?.photo?.isNotEmpty() == true && _playlist.value?.name?.isNotEmpty() == true && _playlist.value?.description?.isNotEmpty() == true)
            _enablePressedCallback.value = true
        if(_playlist.value?.name != "")
            _buttonCreateEnable.value = true
        else
            _buttonCreateEnable.value = false
    }

    fun createPlaylist(){
        Log.v("my",_playlist.value.toString()+" tempPlaylist")
        createJob?.cancel()
        createJob = viewModelScope.launch {
            _playlist.value?.let {
                database.setPlaylist(it)
                createdMessage(it)
                _enablePressedCallback.postValue(false)
                _closeFragment.postValue(true)
            }
        }
    }

    fun getPlaylist():Playlist = _playlist.value!!

    private fun createdMessage(playlist:Playlist){
        Toast.makeText(context,
            "${context.getString(R.string.create_one)} " +
                    "${playlist.name} ${context.getString(R.string.create_two)}",Toast.LENGTH_SHORT)
            .show()
    }
}