package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.search.ui.models.SearchState
import kotlinx.coroutines.flow.Flow

interface TracksInteractor {
    suspend fun searchTracksSuspend(expression: String): Flow<SearchState<List<Track>>>
}