package com.example.playlistmaker.common.data.db.converters

import com.example.playlistmaker.common.data.db.entity.TrackEntityForPlaylist
import com.example.playlistmaker.common.domain.models.Track

class TrackEntityForPlaylistDbConverter {
    fun map(track: Track): TrackEntityForPlaylist {
        return with(track) {
            TrackEntityForPlaylist(
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

    fun map(track: TrackEntityForPlaylist): Track {
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
                trackId
            )
        }
    }
}