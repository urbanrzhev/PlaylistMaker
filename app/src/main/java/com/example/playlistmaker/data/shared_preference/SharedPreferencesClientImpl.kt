package com.example.playlistmaker.data.shared_preference

import android.content.Context
import com.example.playlistmaker.data.dto.TrackDto
import com.example.playlistmaker.domain.util.MyConstants
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharedPreferencesClientImpl(
    private val context: Context
) : SharedPreferencesClient {
    private val keyPreferences = "my_all_preferences"
    private val gson = Gson()
    private val sharedPreferences =
        context.getSharedPreferences(keyPreferences, MyConstants.MODE_PRIVATE)

    override fun getBoolean(key: String): Boolean =
        sharedPreferences.getBoolean(key, false)


    override fun setBoolean(key: String,data:Boolean) {
        sharedPreferences.edit()
            .putBoolean(key, data)
            .apply()
    }

    override fun getTrack(key:String): TrackDto? {
        return gson.fromJson(
            sharedPreferences.getString(
                key,
                null
            ), TrackDto::class.java
        ) ?: null
    }

    override fun setTrack(key: String,data: TrackDto) {
        sharedPreferences.edit()
            .putString(
                key, gson.toJson(
                    data
                )
            )
            .apply()
    }

    override fun getTrackList(key: String): List<TrackDto> {
        val sp = sharedPreferences.getString(key, null) ?: return emptyList()
        val itemType = object : TypeToken<List<TrackDto>>() {}.type
        val trackList = gson.fromJson<List<TrackDto>>(sp, itemType)
        return trackList ?: emptyList()
    }

    override fun setTrackList(key: String, data:List<TrackDto>) {
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








