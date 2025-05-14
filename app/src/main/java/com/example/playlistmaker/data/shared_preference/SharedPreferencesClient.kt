package com.example.playlistmaker.data.shared_preference

import com.example.playlistmaker.data.dto.TrackDto

interface SharedPreferencesClient {
    fun getBoolean(key: String):Boolean
    fun setBoolean(key: String,data:Boolean)
    fun getTrack(key:String):TrackDto?
    fun setTrack(key: String,data: TrackDto)
    fun getTrackList(key: String):List<TrackDto>
    fun setTrackList(key: String, data:List<TrackDto>)
    fun clearTrackList(key: String,clear:Boolean)
}
