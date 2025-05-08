package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.dto.TracksSearchRequest
import com.example.playlistmaker.data.dto.TracksSearchResponse
import com.example.playlistmaker.domain.TimeFormat
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.models.TrackData

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {

    override fun searchTracks(expression: String): TrackData {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        if (response.resultCode == 200) {
            val data = response as TracksSearchResponse
            return TrackData(resultCodeResponse =  response.resultCode, resultCount =  data.results.size, results =  data.results.map {
                Track(
                    trackName = it.trackName,
                    artistName = it.artistName,
                    trackTimeNormal = TimeFormat(it.trackTimeMillis).getTimeMM_SS(),
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
        } else {
            return TrackData(resultCodeResponse = response.resultCode, resultCount =  0, results =  emptyList())
        }
    }
}