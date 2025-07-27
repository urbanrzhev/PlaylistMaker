package com.example.playlistmaker.search.ui.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.common.util.SingleLiveEvent
import com.example.playlistmaker.search.domain.api.HistoryInteractor
import com.example.playlistmaker.search.domain.api.SetActiveTrackUseCase
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.ui.models.SearchState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(
    private val setActiveTrackUseCase: SetActiveTrackUseCase,
    private val tracksInteractor: TracksInteractor,
    private val searchHistoryInteractor: HistoryInteractor
) : ViewModel() {
    private var job: Job? = null
    private val handler = Handler(Looper.getMainLooper())
    private var temporaryTextRequest = ""
    private var focusEditText = MutableLiveData<Boolean>()
    fun observeFocusEditTextLiveData():LiveData<Boolean> = focusEditText
    private var temporaryEditText = MutableLiveData<String>()
    fun observeTemporaryEditTextLiveData():LiveData<String> = temporaryEditText
    private var searchStateLiveData = MutableLiveData<SearchState<List<Track>>>()
    fun observeState(): LiveData<SearchState<List<Track>>> = searchStateLiveData
    private var historyLiveData = SingleLiveEvent<List<Track>>()
    fun observeHistory(): LiveData<List<Track>> = historyLiveData

    fun setFocusEditText(value:Boolean){
        focusEditText.value = value
    }

    fun setTemporaryEditText(value:String){
        temporaryEditText.value = value
    }

    fun searchDebounce() {
        searchDebounce(temporaryTextRequest,true)
    }

    fun clearSearchDebounce() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    fun searchDebounce(requestText: String, reload:Boolean = false) {
        if(temporaryTextRequest != requestText || reload) {
            temporaryTextRequest = requestText
            job?.cancel()
            job = viewModelScope.launch {
                delay(SEARCH_DEBOUNCE_DELAY)
                searchRequest(requestText)
            }
        }
    }

    fun clearTemporaryTextRequest(){
        temporaryTextRequest = ""
    }

    private fun searchRequest(
        requestText: String
    ) {
        if (requestText.trim().isNotEmpty()) {
            searchTracks(requestText)
        }
    }

    fun visibleHistory(){
        searchStateLiveData.value = SearchState.History()
    }

    fun getHistory(): List<Track> {
        return searchHistoryInteractor.getTracks()
    }

    fun addTrackHistory(track: Track) {
        searchHistoryInteractor.addTrack(track) {
            historyLiveData.value = it
        }
    }

    fun clearTrackListHistory() {
        searchHistoryInteractor.clear()
    }

    fun setActiveTrack(track: Track) {
        setActiveTrackUseCase.execute(track)
    }

    private fun searchTracks(expression: String) {
        searchStateLiveData.value = SearchState.Loaded()
        tracksInteractor.searchTracks(expression = expression, TracksInteractor.TracksConsumer {
            searchStateLiveData.postValue(it)
        })
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY: Long = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()
    }
}