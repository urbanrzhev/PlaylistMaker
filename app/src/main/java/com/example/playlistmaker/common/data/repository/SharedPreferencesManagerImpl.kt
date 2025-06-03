package com.example.playlistmaker.common.data.repository

import android.content.SharedPreferences
import com.example.playlistmaker.common.domain.api.SharedPreferencesManager
import com.example.playlistmaker.common.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharedPreferencesManagerImpl(
    private val sharedPreferences: SharedPreferences,
    private val gson:Gson
): SharedPreferencesManager {
    override fun getBoolean(key: String): Boolean =
        sharedPreferences.getBoolean(key, false)

    override fun setBoolean(key: String,data:Boolean) {
        sharedPreferences.edit()
            .putBoolean(key, data)
            .apply()
    }

    override fun getTrack(key:String): Track {
        return gson.fromJson(
            sharedPreferences.getString(
                key,
                null
            ), Track::class.java
        ) ?: Track()
    }

    override fun setTrack(key: String,data: Track) {
        sharedPreferences.edit()
            .putString(
                key, gson.toJson(
                    data
                )
            )
            .apply()
    }

    override fun getTrackList(key: String): List<Track> {
        val sp = sharedPreferences.getString(key, null) ?: return mutableListOf()
        val itemType = object : TypeToken<List<Track>>() {}.type
        val trackList = gson.fromJson<List<Track>>(sp, itemType)
        return trackList ?: mutableListOf()
    }

    override fun setTrackList(key: String, data:List<Track>) {
        sharedPreferences.edit()
            .putString(
                key, gson.toJson(
                    data
                )
            )
            .apply()
    }

    override fun clearTrackList(key: String, clear:Boolean) {
        if (clear)
            sharedPreferences.edit().remove(key)
                .apply()
    }
}