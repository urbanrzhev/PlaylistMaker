package com.example.playlistmaker.common.db.converters

import com.example.playlistmaker.common.db.entity.TrackInPlaylistEntity
import com.example.playlistmaker.common.domain.models.Track

class TrackInPlaylistEntityDbConverter {
    fun map(track: Track): TrackInPlaylistEntity {
        return with(track) {
            TrackInPlaylistEntity(
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