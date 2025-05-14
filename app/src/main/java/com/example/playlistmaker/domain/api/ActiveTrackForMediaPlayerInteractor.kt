package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface ActiveTrackForMediaPlayerInteractor {
    fun getActiveTrackForMediaPlayer(
        consumer: TrackConsumer
    )
    fun setActiveTrackForMediaPlayer(data: Track)
    fun interface TrackConsumer {
        fun consume(response: Track)
    }
}