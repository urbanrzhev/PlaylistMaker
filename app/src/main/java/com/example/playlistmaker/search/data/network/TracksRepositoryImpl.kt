package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.R
import com.example.playlistmaker.search.data.dto.TracksSearchRequest
import com.example.playlistmaker.search.data.dto.TracksSearchResponse
import com.example.playlistmaker.common.util.TimeFormat
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.search.domain.models.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TracksRepositoryImpl(private val networkClient: NetworkClient, private val timeFormat: TimeFormat) : TracksRepository {
    override fun searchTracks(expression: String): Flow<Resource<List<Track>>> {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        return response.map { result ->
            when (result.resultCode) {
                -1 -> Resource.Error(message = R.string.load_error_one_for_search_double)
                200 ->
                    Resource.Success(data = (result as TracksSearchResponse).results.map {
                        with(it) {
                            Track(
                                trackName = trackName,
                                artistName = artistName,
                                trackTimeNormal = timeFormat.getTimeMM_SS(trackTimeMillis),
                                artworkUrl100 = artworkUrl100,
                                previewUrl = previewUrl,
                                collectionName = collectionName,
                                releaseDate = releaseDate,
                                primaryGenreName = primaryGenreName,
                                country = country,
                                trackId = trackId
                            )
                        }
                    })

                else -> Resource.Error(message = R.string.load_error_two_for_search_double)
            }
        }
    }
}