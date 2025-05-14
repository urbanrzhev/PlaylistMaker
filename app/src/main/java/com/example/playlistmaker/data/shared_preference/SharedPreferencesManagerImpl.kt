package com.example.playlistmaker.data.shared_preference

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.example.playlistmaker.domain.api.SharedPreferencesManager
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.activity.App
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

private const val MY_ALL_PREFERENCES = "my_all_preferences"
class SharedPreferencesManagerImpl(): SharedPreferencesManager {
    private val context:Context = App.appContext
    private val gson = Gson()
    private val sharedPreferences =
        context.getSharedPreferences(MY_ALL_PREFERENCES, MODE_PRIVATE)

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
        val sp = sharedPreferences.getString(key, null) ?: return emptyList()
        val itemType = object : TypeToken<List<Track>>() {}.type
        val trackList = gson.fromJson<List<Track>>(sp, itemType)
        return trackList ?: emptyList()
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








