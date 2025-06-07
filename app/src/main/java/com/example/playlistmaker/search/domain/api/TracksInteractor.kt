package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.search.domain.models.State

interface TracksInteractor {
    fun searchTracks(expression: String, consumer:TracksInteractor.TracksConsumer)

    fun interface TracksConsumer {
        fun consume(foundTracks: State<List<Track>>)
    }
}