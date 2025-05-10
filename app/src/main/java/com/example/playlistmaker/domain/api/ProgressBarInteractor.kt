package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.TrackData

interface ProgressBarInteractor {
    fun searchTracks(expression: String, consumer: TracksConsumer)

    interface TracksConsumer {
        fun consume(foundTracks: TrackData)
    }
}