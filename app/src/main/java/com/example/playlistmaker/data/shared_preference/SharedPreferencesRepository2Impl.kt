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

    override fun setMediaPlayerLoadStartActivity(dto:SharedPreferencesBooleanRequest) {
        sharedPreferencesClient.setBooleanData(dto)
    }

    override fun getAppDarkTheme(dto:SharedPreferencesBooleanRequest): Boolean {
           val result = sharedPreferencesClient.getBooleanRequest(dto)
        return result.result?:false
    }

    override fun setAppDarkTheme(dto:SharedPreferencesBooleanRequest) {
        sharedPreferencesClient.setBooleanData(dto) 

    }

    override fun getActiveTrackForMediaPlayer(dto:SharedPreferencesTrackRequest): Track{
           val result = sharedPreferencesClient.getTrackRequest(dto)
           return if(result != null){
               // time format
                results as Track
              } else Track() 
        }


    override fun setActiveTrackForMediaPlayer(dto:SharedPreferencesTrackRequest) {
        sharedPreferencesClient.setTrackData(dto)
    }

    override fun getSearchHistoryTrackList(dto:SharedPreferencesTrackListRequest ): List<Track> {
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
        return emptyList()
    }

    override fun setSearchHistoryTrackList(dto:SharedPreferencesTrackListRequest) {
                 sharedPreferencesClient.setTrackListData(dto)
    }

    override fun clearSearchHistoryTrackList(dto:SharedPreferencesTrackListRequest) {
   sharedPreferencesClient.clearTrackListData(dto)
    }
}




    
