package com.example.playlistmaker.common.domain.impl

import com.example.playlistmaker.common.domain.api.DataBaseFavoritesTracksInteractor
import com.example.playlistmaker.common.domain.api.DataBaseFavoritesTracksRepository
import com.example.playlistmaker.common.domain.models.Track
import kotlinx.coroutines.flow.Flow

class DataBaseFavoritesTracksInteractorImpl(
    private val dbrepository:DataBaseFavoritesTracksRepository
) :DataBaseFavoritesTracksInteractor{
    override suspend fun setFavoriteTrack(track: Track) {
        dbrepository.setFavoriteTrack(track)
    }

    override suspend fun deleteFavoriteTrack(trackId: Int) {
        dbrepository.deleteFavoriteTrack(trackId)
    }

    override fun checkTrackInFavorites(trackId: Int): Flow<Boolean> {
        return dbrepository.checkTrackInFavorites(trackId)
    }

    override fun getAllFavoritesTracks(): Flow<List<Track>> {
        return dbrepository.getAllFavoritesTracks()
    }
}