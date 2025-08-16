package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.search.domain.models.Resource
import kotlinx.coroutines.flow.Flow

interface TracksInteractor {
    fun searchTracks(expression: String): Flow<Resource<List<Track>>>
}