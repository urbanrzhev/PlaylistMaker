package com.example.playlistmaker.search.ui.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.common.domain.api.SetStartActivityUseCase
import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.search.domain.api.HistoryInteractor
import com.example.playlistmaker.search.domain.api.SetActiveTrackUseCase
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.ui.models.SearchState

class SearchViewModel(
    private val setActiveTrackUseCase: SetActiveTrackUseCase,
    private val tracksInteractor: TracksInteractor,
    private val searchHistoryInteractor: HistoryInteractor,
    private val setStartActivityUseCase: SetStartActivityUseCase
) : ViewModel() {
    private val handler = Handler(Looper.getMainLooper())
    private var temporaryTextRequest = ""
    private val progressBarLiveData = MutableLiveData(false)
    fun observerProgressBarLiveData(): LiveData<Boolean> = progressBarLiveData
    private var searchStateLiveData = MutableLiveData<SearchState<List<Track>>>(SearchState.Loaded())
    fun observeState(): LiveData<SearchState<List<Track>>> = searchStateLiveData
    private var historyLiveData = MutableLiveData(getHistory())
    fun observeHistory(): LiveData<List<Track>> = historyLiveData

    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    fun searchDebounce() {
        searchDebounce(temporaryTextRequest)
    }

    fun clearSearchDebounce() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    fun searchDebounce(requestText: String) {
        temporaryTextRequest = requestText
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        val searchRunnable = Runnable { searchRequest(requestText) }
        handler.postDelayed(searchRunnable, SEARCH_REQUEST_TOKEN, SEARCH_DEBOUNCE_DELAY)
    }

    fun setStartActivity(){
        setStartActivityUseCase.execute(MAIN_ACTIVITY)
    }
    private fun searchRequest(
        requestText: String
    ) {
        if (requestText.isNotEmpty()) {
            searchTracks(requestText)
        }
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
        progressBarLiveData.value = true
        tracksInteractor.searchTracks(expression = expression, TracksInteractor.TracksConsumer {
            searchStateLiveData.postValue(it)
            progressBarLiveData.postValue(false)
        })
    }

    companion object {
        private const val MAIN_ACTIVITY = "main"
        private const val SEARCH_DEBOUNCE_DELAY: Long = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()
    }
}