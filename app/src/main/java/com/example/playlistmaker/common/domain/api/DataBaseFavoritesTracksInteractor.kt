package com.example.playlistmaker.common.domain.api

import com.example.playlistmaker.common.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface DataBaseFavoritesTracksInteractor {
    suspend fun setFavoriteTrack(track:Track)
    suspend fun deleteFavoriteTrack(trackId:Int)
    fun checkTrackInFavorites(trackId: Int):Flow<Boolean>
    fun getAllFavoritesTracks():Flow<List<Track>>
}