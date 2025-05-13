package com.example.playlistmaker.domain.api

import com.example.playlistmaker.data.dto.SharedPreferencesBooleanRequest
import com.example.playlistmaker.domain.models.Track

interface SharedPreferencesRepository2 {
    fun getMediaPlayerLoadStartActivity(dto:SharedPreferencesBooleanRequest): Boolean
    fun setMediaPlayerLoadStartActivity(keyData: Boolean)
    fun getAppDarkTheme(): Boolean
    fun setAppDarkTheme(keyData: Boolean)
    fun getActiveTrackForMediaPlayer(): Track
    fun setActiveTrackForMediaPlayer(keyData: Track)
    fun getSearchHistoryTrackList(): MutableList<Track>
    fun setSearchHistoryTrackList(trackList: MutableList<Track>)
    fun clearSearchHistoryTrackList()

}