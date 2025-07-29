package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.R
import com.example.playlistmaker.search.data.dto.TracksSearchRequest
import com.example.playlistmaker.search.data.dto.TracksSearchResponse
import com.example.playlistmaker.common.util.TimeFormat
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.search.domain.models.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.getKoin

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {
    override suspend fun searchTracksSuspend(expression: String): Flow<Resource<List<Track>>> {
        val response = networkClient.doRequestSuspend(TracksSearchRequest(expression))
        return flow {
            when (response.resultCode) {
                -1 -> emit(Resource.Error(message = R.string.load_error_one_for_search_double))
                200 -> emit(Resource.Success(data = (response as TracksSearchResponse).results.map {
                    Track(
                        trackName = it.trackName,
                        artistName = it.artistName,
                        trackTimeNormal = getKoin().get<TimeFormat> {
                            parametersOf(it.trackTimeMillis)
                        }.getTimeMM_SS(),
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
                )

                else -> emit(Resource.Error(message = R.string.load_error_two_for_search_double))
            }
        }
    }
}