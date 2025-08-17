package com.example.playlistmaker.common.data.repository

import android.content.SharedPreferences
import com.example.playlistmaker.common.db.dao.TrackDao
import com.example.playlistmaker.common.domain.api.SharedPreferencesManager
import com.example.playlistmaker.common.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SharedPreferencesManagerImpl(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson,
    private val database: TrackDao
) : SharedPreferencesManager {
    private var dbListFavoritesIds: List<Int>? = null

    init {
        getDbFavoritesTracks()
    }

    override fun getString(key: String): String =
        sharedPreferences.getString(key, "").toString()

    override fun setString(key: String, data: String) {
        sharedPreferences.edit()
            .putString(key, data)
            .apply()
    }

    override fun getBoolean(key: String): Boolean =
        sharedPreferences.getBoolean(key, false)

    override fun setBoolean(key: String, data: Boolean) {
        sharedPreferences.edit()
            .putBoolean(key, data)
            .apply()
    }

    override fun getTrackList(key: String): List<Track> {
        val sp = sharedPreferences.getString(key, null) ?: return mutableListOf()
        val itemType = object : TypeToken<List<Track>>() {}.type
        val trackList = gson.fromJson<List<Track>>(sp, itemType)
        val newTrackList = trackList.map {
            checkFavoriteTrack(it)
        }
        return newTrackList ?: mutableListOf()
    }

    override fun setTrackList(key: String, data: List<Track>) {
        sharedPreferences.edit()
            .putString(
                key, gson.toJson(
                    data
                )
            )
            .apply()
    }

    override fun clearTrackList(key: String, clear: Boolean) {
        if (clear)
            sharedPreferences.edit().remove(key)
                .apply()
    }

    private fun getDbFavoritesTracks(){
        CoroutineScope(Dispatchers.IO).launch {
            dbListFavoritesIds = database.getFavoritesTrackIds()
        }
    }

    private fun checkFavoriteTrack(track: Track): Track {
        var check = false
        dbListFavoritesIds?.forEach {
            if (track.trackId == it)
                check = true
        }
        return track.copy(
            isFavorite = check
        )
    }
}