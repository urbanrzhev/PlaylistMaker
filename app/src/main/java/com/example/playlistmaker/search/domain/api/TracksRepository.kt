package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.search.domain.models.Resource

interface TracksRepository {
    fun searchTracks(expression: String): Resource<List<Track>>
}