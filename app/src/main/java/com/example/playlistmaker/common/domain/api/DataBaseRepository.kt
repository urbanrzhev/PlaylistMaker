package com.example.playlistmaker.common.domain.api

import com.example.playlistmaker.common.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface DataBaseRepository {
    suspend fun setFavoriteTrack(track:Track)
    suspend fun deleteFavoriteTrack(trackId:Int)
    fun getAllFavoritesTracks():Flow<List<Track>>
}