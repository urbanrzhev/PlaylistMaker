package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.search.domain.models.Resource

interface TracksInteractor {
    fun searchTracks(expression: String, consumer:TracksInteractor.TracksConsumer)

    fun interface TracksConsumer {
        fun consume(foundTracks: Resource<List<Track>>)
    }
}