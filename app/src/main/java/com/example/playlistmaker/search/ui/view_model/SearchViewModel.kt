package com.example.playlistmaker.search.ui.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.common.db.dao.TrackDao
import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.common.util.debounce
import com.example.playlistmaker.search.domain.api.HistoryInteractor
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.models.Resource
import com.example.playlistmaker.search.ui.models.SearchState
import kotlinx.coroutines.launch

class SearchViewModel(
    private val tracksInteractor: TracksInteractor,
    private val searchHistoryInteractor: HistoryInteractor,
    private val database: TrackDao
) : ViewModel() {
    private val trackSearchDebounce =
        debounce<String>(SEARCH_DEBOUNCE_DELAY, viewModelScope, true) { text ->
            searchRequest(text)
        }
    private val handler = Handler(Looper.getMainLooper())
    private var temporaryTextRequest = ""
    private var focusEditText = MutableLiveData<Boolean>()
    fun observeFocusEditTextLiveData(): LiveData<Boolean> = focusEditText
    private var temporaryEditText = MutableLiveData<String>()
    fun observeTemporaryEditTextLiveData(): LiveData<String> = temporaryEditText
    private var searchStateLiveData = MutableLiveData<SearchState<List<Track>>>()
    fun observeState(): LiveData<SearchState<List<Track>>> = searchStateLiveData
    private var historyLiveData = MutableLiveData<List<Track>>(getHistory())
    fun observeHistory(): LiveData<List<Track>> = historyLiveData

    fun updateListsRecycler() {
        viewModelScope.launch {
            val databaseFavoritesIdsList = database.getFavoritesTrackIds()
            searchStateLiveData.value?.let { list ->
                if (list is SearchState.Success) {
                    val newList = newListIdsMapper(list.data, databaseFavoritesIdsList)
                    searchStateLiveData.value = SearchState.Success(newList)
                }
            }
            historyLiveData.value?.let { list ->
                val newList = newListIdsMapper(list, databaseFavoritesIdsList)
                historyLiveData.value = newList
            }
        }
    }

    private fun newListIdsMapper(list: List<Track>, ids: List<Int>): List<Track> {
        return list.map { track ->
            track.copy(
                isFavorite = run {
                    var check = false
                    ids.forEach { id ->
                        if (track.trackId == id)
                            check = true
                    }
                    return@run check
                }
            )
        }
    }

    fun setFocusEditText(value: Boolean) {
        focusEditText.value = value
    }

    fun setTemporaryEditText(value: String) {
        temporaryEditText.value = value
    }

    fun searchDebounce() {
        searchDebounce(temporaryTextRequest, true)
    }

    fun clearSearchDebounce() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    fun searchDebounce(requestText: String, reload: Boolean = false) {
        if (temporaryTextRequest != requestText || reload) {
            temporaryTextRequest = requestText
            trackSearchDebounce(requestText)
        }
    }

    fun clearTemporaryTextRequest() {
        temporaryTextRequest = ""
    }

    private fun searchRequest(
        requestText: String
    ) {
        if (requestText.trim().isNotEmpty()) {
            searchTracks(requestText)
        }
    }

    fun visibleHistory() {
        searchStateLiveData.value = SearchState.History()
    }

    private fun getHistory(): List<Track> {
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

    private fun searchTracks(expression: String) {
        searchStateLiveData.value = SearchState.Loaded()
        viewModelScope.launch {
            tracksInteractor.searchTracks(expression).collect {
                when (it) {
                    is Resource.Success -> {
                        if (it.data.isNotEmpty())
                            searchStateLiveData.postValue(SearchState.Success(it.data))
                        else
                            searchStateLiveData.postValue(SearchState.Idle())
                    }

                    is Resource.Error -> {
                        searchStateLiveData.postValue(
                            SearchState.Error(
                                it.message
                            )
                        )
                    }
                }
            }
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY: Long = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()
    }
}