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

    override fun getActiveTrackForMediaPlayer(dto:SharedPreferencesStringRequest): Track{
           val result = sharedPreferencesClient.getTrackRequest(dto)
           return if(result != null){
               // time format
                results as Track
              } else Track() 
        }


    override fun setActiveTrackForMediaPlayer(keyData: Track) {

    }

    override fun getSearchHistoryTrackList(dto:SharedPreferencesStringRequest ): MutableList<Track> {
        val result = sharedPreferencesClient.getTrackListRequest(dto)
        // time format
        val res = result.map{
            Track(
                        trackName = it.trackName,
                        artistName = it.artistName,
                        trackTimeNormal = it.trackTimeNormal//TimeFormat(it.trackTimeMillis).getTimeMM_SS(),
                        artworkUrl100 = it.artworkUrl100,
                        previewUrl = it.previewUrl,
                        collectionName = it.collectionName,
                        releaseDate = it.releaseDate,
                        primaryGenreName = it.primaryGenreName,
                        country = it.country,
                        trackId = it.trackId
                    )
        }
        return mutableListOf()
    }

    override fun setSearchHistoryTrackList(trackList: MutableList<Track>) {

    }

    override fun clearSearchHistoryTrackList() {

    }
}
