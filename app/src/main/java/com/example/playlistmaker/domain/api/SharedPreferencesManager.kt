package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface SharedPreferencesManager {
    fun getBoolean(key: String): Boolean
    fun setBoolean(key: String, data: Boolean)
    fun getTrack(key: String): Track
    fun setTrack(key: String, data: Track)
    fun getTrackList(key: String): List<Track>
    fun setTrackList(key: String, data: List<Track>)
    fun clearTrackList(key:String,clear:Boolean)
}

