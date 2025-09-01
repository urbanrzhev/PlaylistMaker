package com.example.playlistmaker.common.data.db.converters

import com.example.playlistmaker.common.data.db.entity.TrackFavoriteEntity
import com.example.playlistmaker.common.domain.models.Track

class ListFavoriteTrackEntityDbConverter {
    fun map(list:List<TrackFavoriteEntity>):List<Track>{
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
                    trackId,
                    trackTimeMillis
                )
            }
        }
    }
}