package com.example.playlistmaker.data.shared_preference

import com.example.playlistmaker.data.dto.TrackDto
import com.example.playlistmaker.domain.api.SharedPreferencesRepository
import com.example.playlistmaker.domain.models.Track

class SharedPreferencesRepositoryImpl(private val sharedPreferencesClient: SharedPreferencesClient) :
    SharedPreferencesRepository {
    override fun getBoolean(key: String): Boolean {
        val result = sharedPreferencesClient.getBoolean(
                key = key
        )
        return result
    }

    override fun setBoolean(key: String, data: Boolean) {
        sharedPreferencesClient.setBoolean(
                key = key,
                data = data
        )
    }

    override fun getTrack(key: String): Track {
        val result = sharedPreferencesClient.getTrack(
            key
        )
        return if (result != null) Track(
            trackName = result.trackName,
            artistName = result.artistName,
            trackTimeNormal = result.trackTimeNormal,
            artworkUrl100 = result.artworkUrl100,
            previewUrl = result.previewUrl,
            collectionName = result.collectionName,
            releaseDate = result.releaseDate,
            primaryGenreName = result.primaryGenreName,
            country = result.country,
            trackId = result.trackId
        ) else Track()
    }

    override fun setTrack(key: String, data: Track) {
        val data2 = TrackDto(
            trackName = data.trackName,
            artistName = data.artistName,
            trackTimeNormal = data.trackTimeNormal,
            artworkUrl100 = data.artworkUrl100,
            previewUrl = data.previewUrl,
            collectionName = data.collectionName,
            releaseDate = data.releaseDate,
            primaryGenreName = data.primaryGenreName,
            country = data.country,
            trackId = data.trackId
        )
        sharedPreferencesClient.setTrack(
                key = key,
                data = data2
        )
    }

    override fun getTrackList(key: String): List<Track> {
        val result = sharedPreferencesClient.getTrackList(
                key = key
        )
        val res = result.map {
            Track(
                trackName = it.trackName,
                artistName = it.artistName,
                trackTimeNormal = it.trackTimeNormal,
                artworkUrl100 = it.artworkUrl100,
                previewUrl = it.previewUrl,
                collectionName = it.collectionName,
                releaseDate = it.releaseDate,
                primaryGenreName = it.primaryGenreName,
                country = it.country,
                trackId = it.trackId
            )
        }
        return res
    }

    override fun setTrackList(key: String, data: List<Track>) {
        sharedPreferencesClient.setTrackList(
            key = key,
            data = data.map {
                TrackDto(
                    trackName = it.trackName,
                    artistName = it.artistName,
                    trackTimeNormal = it.trackTimeNormal,
                    artworkUrl100 = it.artworkUrl100,
                    previewUrl = it.previewUrl,
                    collectionName = it.collectionName,
                    releaseDate = it.releaseDate,
                    primaryGenreName = it.primaryGenreName,
                    country = it.country,
                    trackId = it.trackId
                )
            }
        )
    }

    override fun clearTrackList(key: String, clear: Boolean) {
        sharedPreferencesClient.clearTrackList(
                key = key,
                clear = clear
            )
    }
}




    
