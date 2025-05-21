package com.example.playlistmaker.data.network

import com.example.playlistmaker.R
import com.example.playlistmaker.data.dto.TracksSearchRequest
import com.example.playlistmaker.data.dto.TracksSearchResponse
import com.example.playlistmaker.domain.util.TimeFormat
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.util.Creator
import com.example.playlistmaker.util.Resource

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {

    override fun searchTracks(expression: String): Resource<List<Track>> {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        val data = response as TracksSearchResponse
        return when (response.resultCode) {
            -1 -> Resource.Error(message = R.string.load_error_one_for_search_double)
            200 -> Resource.Success(data = data.results.map {
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

            else -> Resource.Error(message = R.string.load_error_two_for_search_double)
        }
    }
}