package com.example.playlistmaker.media_library.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.common.domain.api.DataBaseInteractor
import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.media_library.ui.models.RecyclerState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class FavoritesTracksViewModel(
    private val dataBaseInteractor: DataBaseInteractor
): ViewModel() {
    private var job: Job? = null
    private val listTracks = MutableLiveData<RecyclerState<List<Track>>>(RecyclerState.Idle())
    fun observeState(): LiveData<RecyclerState<List<Track>>> = listTracks

    fun updateFavoritesTracks(){
        job?.cancel()
        job = viewModelScope.launch {
            dataBaseInteractor.getAllFavoritesTracks().collect {
                when {
                    it.isNotEmpty() -> listTracks.postValue(RecyclerState.Success(it))
                    else -> listTracks.postValue(RecyclerState.Empty())
                }
            }
        }
    }
}