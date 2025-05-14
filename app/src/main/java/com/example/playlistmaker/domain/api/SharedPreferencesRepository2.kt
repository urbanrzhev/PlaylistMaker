package com.example.playlistmaker.domain.api

import com.example.playlistmaker.data.dto.SharedPreferencesBooleanRequest
import com.example.playlistmaker.domain.models.Track

interface SharedPreferencesRepository2 {
   fun getBoolean(key:String) 
    //fun getMediaPlayerLoadStartActivity(dto:SharedPreferencesBooleanRequest): Boolean
    fun setMediaPlayerLoadStartActivity(dto:SharedPreferencesBooleanRequest)
    fun getAppDarkTheme(dto:SharedPreferencesBooleanRequest): Boolean
    fun setAppDarkTheme(dto:SharedPreferencesBooleanRequest)
    fun getActiveTrackForMediaPlayer(dto:SharedPreferencesTrackRequest): Track
    fun setActiveTrackForMediaPlayer(dto:SharedPreferencesTrackRequest)
    fun getSearchHistoryTrackList(dto:SharedPreferencesTrackListRequest):List<Track>
    fun setSearchHistoryTrackList(dto:SharedPreferencesTrackListRequest)
    fun clearSearchHistoryTrackList(dto:SharedPreferencesTrackListRequest)

}
