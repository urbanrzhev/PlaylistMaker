package com.example.playlistmaker.common.domain.impl

import com.example.playlistmaker.common.domain.api.DataBaseTracksInteractor
import com.example.playlistmaker.common.domain.api.DataBaseTracksRepository
import com.example.playlistmaker.common.domain.models.Track
import kotlinx.coroutines.flow.Flow

class DataBaseTracksInteractorImpl(
    private val dbrepository:DataBaseTracksRepository
) :DataBaseTracksInteractor{
    override suspend fun setFavoriteTrack(track: Track) {
        dbrepository.setFavoriteTrack(track)
    }

    override suspend fun deleteFavoriteTrack(trackId: Int) {
        dbrepository.deleteFavoriteTrack(trackId)
    }

    override fun getAllFavoritesTracks(): Flow<List<Track>> {
        return dbrepository.getAllFavoritesTracks()
    }
}