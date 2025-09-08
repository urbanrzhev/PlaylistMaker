package com.example.playlistmaker.common.data.db.converters

import com.example.playlistmaker.common.data.db.entity.TrackFavoriteEntity
import com.example.playlistmaker.common.domain.models.Track

class TrackEntityDbConverter {
    fun map(track: Track): TrackFavoriteEntity {
        return with(track) {
            TrackFavoriteEntity(
                trackName,
                artistName,
                trackTimeNormal,
                artworkUrl100,
                previewUrl,
                collectionName,
                releaseDate,
                primaryGenreName,
                country,
                trackTimeMillis,
                trackId,
            )
        }
    }

    fun map(track: TrackFavoriteEntity): Track {
        return with(track) {
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
                trackTimeMillis,
                isFavorite = true
            )
        }
    }
}
