package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface SharedPreferencesInteractor {
    fun getStartActivity(
        consumer: BooleanConsumer
    )
    fun setStartActivity(keyData: Boolean)
    fun getAppDarkTheme(
        consumer: BooleanConsumer
    )
    fun setAppDarkTheme(keyData: Boolean)
    fun getActiveTrackForMediaPlayer(
        consumer: TrackConsumer
    )
    fun setActiveTrackForMediaPlayer(keyData: Track)
    fun getSearchHistoryTrackList(consumer: TrackListConsumer)
    fun setSearchHistoryTrackList(trackList: MutableList<Track>)
    fun clearSearchHistoryTrackList()

    fun interface BooleanConsumer {
        fun consume(response: Boolean)
    }
    fun interface TrackConsumer {
        fun consume(response: Track)
    }
    fun interface TrackListConsumer {
        fun consume(response: MutableList<Track>)
    }
}
