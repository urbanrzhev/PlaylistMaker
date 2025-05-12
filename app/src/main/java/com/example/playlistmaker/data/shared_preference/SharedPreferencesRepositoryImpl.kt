package com.example.playlistmaker.data.shared_preference

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.example.playlistmaker.domain.api.SharedPreferencesRepository
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.util.MyConstants
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharedPreferencesRepositoryImpl(context: Context) :
    SharedPreferencesRepository {
    private val gson = Gson()
    private val sharedPreferences = context.getSharedPreferences(MyConstants.MY_ALL_PREFERENCES,MODE_PRIVATE)

    override fun getMediaPlayerLoadStartActivity(): Boolean {
        return sharedPreferences.getBoolean(MyConstants.MY_KEY_ON_OFF_MEDIA_LIBRARY_ACTIVITY, false)
    }

    override fun setMediaPlayerLoadStartActivity(keyData: Boolean) {
        sharedPreferences.edit()
            .putBoolean(MyConstants.MY_KEY_ON_OFF_MEDIA_LIBRARY_ACTIVITY,keyData)
            .apply()
    }


    override fun getAppDarkTheme(): Boolean {
        return sharedPreferences.getBoolean(MyConstants.MY_KEY_SWITCHER,false)
    }

    override fun setAppDarkTheme(keyData: Boolean) {
        sharedPreferences.edit()
            .putBoolean(MyConstants.MY_KEY_SWITCHER, keyData)
            .apply()
    }
    override fun getActiveTrackForMediaPlayer(): Track {
        return gson.fromJson(
            sharedPreferences.getString(
                MyConstants.MY_KEY_DEFAULT_TRACK_MEDIA_LIBRARY_ACTIVITY,
                null
            ), Track::class.java
        ) ?: Track()
    }

    override fun setActiveTrackForMediaPlayer(keyData: Track) {
        sharedPreferences.edit()
            .putString(
                MyConstants.MY_KEY_DEFAULT_TRACK_MEDIA_LIBRARY_ACTIVITY, gson.toJson(
                    keyData
                )
            )
            .apply()
    }

    override fun getSearchHistoryTrackList(): MutableList<Track> {
        val sp = sharedPreferences.getString(MyConstants.KEY_HISTORY_TRACK_LIST, null) ?: return mutableListOf()
        val itemType = object : TypeToken<MutableList<Track>>() {}.type
        val trackList = gson.fromJson<MutableList<Track>>(sp, itemType)
        return trackList
    }

    override fun setSearchHistoryTrackList(trackList: MutableList<Track>) {
        sharedPreferences.edit()
            .putString(MyConstants.KEY_HISTORY_TRACK_LIST, gson.toJson(trackList))
            .apply()
    }

    override fun clearSearchHistoryTrackList() {
        sharedPreferences.edit().remove(MyConstants.KEY_HISTORY_TRACK_LIST).apply()
    }
}