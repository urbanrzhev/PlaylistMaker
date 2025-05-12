package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.TrackData

interface TracksRepository {
    fun searchTracks(expression: String): TrackData
}