package com.example.playlistmaker.data.shared_preference

import com.example.playlistmaker.data.dto.SharedPreferencesBooleanRequest
import com.example.playlistmaker.data.dto.SharedPreferencesBooleanResponse
import com.example.playlistmaker.domain.api.SharedPreferencesRepository
import com.example.playlistmaker.domain.api.SharedPreferencesRepository2
import com.example.playlistmaker.domain.models.Track

class SharedPreferencesRepository2Impl(private val sharedPreferencesClient: SharedPreferencesClient):
    SharedPreferencesRepository2 {
    override fun getMediaPlayerLoadStartActivity(dto:SharedPreferencesBooleanRequest): Boolean {
        val result = sharedPreferencesClient.getBooleanRequest(dto)
        return result.result?:false
    }

    override fun setMediaPlayerLoadStartActivity(keyData: Boolean) {

    }

    override fun getAppDarkTheme(dto:SharedPreferencesBooleanRequest): Boolean {
           val result = sharedPreferencesClient.getBooleanRequest(dto)
        return result.result?:false
    }

    override fun setAppDarkTheme(keyData: Boolean) {

    }

    override fun getActiveTrackForMediaPlayer(): Track{
        return Track()
    }

    override fun setActiveTrackForMediaPlayer(keyData: Track) {

    }

    override fun getSearchHistoryTrackList(): MutableList<Track> {
        return mutableListOf()
    }

    override fun setSearchHistoryTrackList(trackList: MutableList<Track>) {

    }

    override fun clearSearchHistoryTrackList() {

    }
}
