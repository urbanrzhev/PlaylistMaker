package com.example.playlistmaker.common.db.converters

import com.example.playlistmaker.common.db.entity.TrackEntity
import com.example.playlistmaker.common.domain.models.Track

class TrackDbConverter {
    fun map(track: Track): TrackEntity {
        return with(track) {
            TrackEntity(
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

    fun map(track: TrackEntity): Track {
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
                isFavorite = true
            )
        }
    }
}
