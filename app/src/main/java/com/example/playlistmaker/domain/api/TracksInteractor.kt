package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.TrackData

interface TracksInteractor {
    fun searchTracks(expression: String, consumer: TracksConsumer)

    fun interface TracksConsumer {
        fun consume(foundTracks: TrackData)
    }
}