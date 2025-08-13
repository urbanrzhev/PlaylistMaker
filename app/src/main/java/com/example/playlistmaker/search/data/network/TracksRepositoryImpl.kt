package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.R
import com.example.playlistmaker.common.db.AppDatabase
import com.example.playlistmaker.common.db.entity.TrackEntity
import com.example.playlistmaker.search.data.dto.TracksSearchRequest
import com.example.playlistmaker.search.data.dto.TracksSearchResponse
import com.example.playlistmaker.common.util.TimeFormat
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.search.data.dto.TrackDto
import com.example.playlistmaker.search.domain.models.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
    private val timeFormat: TimeFormat,
    private val database: AppDatabase
) : TracksRepository {
    private var list: List<Int>? = null

    override fun searchTracks(expression: String): Flow<Resource<List<Track>>> {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        return response.map { result ->
            when (result.resultCode) {
                -1 -> Resource.Error(message = R.string.load_error_one_for_search_double)
                200 -> {
                    list = database.getTrackDao().getFavoritesTrackIds()
                    Resource.Success(
                        data = (result as TracksSearchResponse).results.map {
                            trackMapper(it)
                        }
                    )
                }

                else -> Resource.Error(message = R.string.load_error_two_for_search_double)
            }
        }
    }

    private fun trackMapper(trackDto: TrackDto): Track {
        return with(trackDto) {
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
                trackId = trackId,
                isFavorite = isFavorite(trackId)
            )
        }
    }

    private fun isFavorite(id: Int): Boolean {
        list?.forEach {
            if (it.equals(id))
                return true
        }
        return false
    }
}