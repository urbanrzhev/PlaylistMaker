package com.example.playlistmaker.common.domain.api

import com.example.playlistmaker.common.domain.models.Track

interface SharedPreferencesManager {
    fun getString(key: String): String
    fun setString(key: String, data: String)
    fun getBoolean(key: String): Boolean
    fun setBoolean(key: String, data: Boolean)
    fun getTrack(key: String): Track
    fun setTrack(key: String, data: Track)
    fun getTrackList(key: String): List<Track>
    fun setTrackList(key: String, data: List<Track>)
    fun clearTrackList(key:String,clear:Boolean)
}

