package com.example.playlistmaker.data.shared_preference

import com.example.playlistmaker.data.dto.SharedPreferencesBooleanRequest
import com.example.playlistmaker.data.dto.SharedPreferencesBooleanResponse
import com.example.playlistmaker.domain.api.SharedPreferencesRepository
import com.example.playlistmaker.domain.api.SharedPreferencesRepository2
import com.example.playlistmaker.domain.models.Track

class SharedPreferencesRepository2Impl(private val sharedPreferencesClient: SharedPreferencesClient):
    SharedPreferencesRepository2 {
    override fun getBoolean/*getMediaPlayerLoadStartActivity*/(key:String): Boolean {
        val result = sharedPreferencesClient.getBooleanRequest(SharedPreferencesBooleanRequest 
                                                               key = key
                                                              )
        return result.result?:false
    }

    override fun setBoolean/*setMediaPlayerLoadStartActivity*/(key:String, data:Boolean) {
        sharedPreferencesClient.setBoolean(SharedPreferencesBooleanRequest 
                                                               key = key, 
                                               data = data
                                                              ))
    }
/*
    override fun getAppDarkTheme(dto:SharedPreferencesBooleanRequest): Boolean {
           val result = sharedPreferencesClient.getBooleanRequest(dto)
        return result.result?:false
    }

    override fun setAppDarkTheme(dto:SharedPreferencesBooleanRequest) {
        sharedPreferencesClient.setBooleanData(dto) 

    }*/

    override fun getTrack/*ActiveTrackForMediaPlayer*/(key:String): Track{
           val result = sharedPreferencesClient.getTrackRequest(SharedPreferencesTrackRequest(
               key = key
           ) )
           return if(result != null){
               // time format
                results as Track
              } else Track() 
        }


    override fun setTrack/*setActiveTrackForMediaPlayer*/(key:String, data:Track) {
        sharedPreferencesClient.setTrackData(SharedPreferencesTrackRequest(
            key = key, 
            data = data as TrackDto
        ) )
    }

    override fun getTrackList/*SearchHistoryTrackList*/(key:String ): List<Track> {
        val result = sharedPreferencesClient.getTrackListRequest(SharedPreferencesTrackListRequest (
            key = key
        ) )
        // time format
        val res = result.map{
            Track(
                        trackName = it.trackName,
                        artistName = it.artistName,
                //time
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

    override fun setTrackList/*SearchHistoryTrackList*/(key:String, data:List<Track>) {
                 sharedPreferencesClient.setTrackListData(SharedPreferencesTrackListRequest(
                     key = key, 
                     data = data. map{
                         TrackDto(
                        trackName = it.trackName,
                        artistName = it.artistName,
                //time
                        trackTimeNormal = it.trackTimeNormal
                        artworkUrl100 = it.artworkUrl100,
                        previewUrl = it.previewUrl,
                        collectionName = it.collectionName,
                        releaseDate = it.releaseDate,
                        primaryGenreName = it.primaryGenreName,
                        country = it.country,
                        trackId = it.trackId
                    )
                     } 
                 ) )
    }

    override fun clear/*SearchHistory*/TrackList(key:String, del:Boolean) {
   sharedPreferencesClient.clearTrackListData(SharedPreferencesTrackListRequest(
       key = key, 
       clear = del
   ))
    }
}




    
