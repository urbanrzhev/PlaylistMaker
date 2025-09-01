package com.example.playlistmaker.common.data.db.converters

import com.example.playlistmaker.common.data.db.entity.TrackEntity
import com.example.playlistmaker.common.domain.models.Track

class ListTrackEntityDbConverter {
    fun map(list:List<TrackEntity>):List<Track>{
        return list.map {
            with(it){
                Track(
                    trackName,
                    artistName,
                    trackTimeNormal,
                    artworkUrl100,
                    previewUrl,
                    collectionName,
                    releaseDate,
                    primaryGenreName,
                    country,
                    trackId
                )
            }
        }
    }
}