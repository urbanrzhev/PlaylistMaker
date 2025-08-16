package com.example.playlistmaker.common.domain.impl

import com.example.playlistmaker.common.domain.api.DataBaseInteractor
import com.example.playlistmaker.common.domain.api.DataBaseRepository
import com.example.playlistmaker.common.domain.models.Track
import kotlinx.coroutines.flow.Flow

class DataBaseInteractorImpl(
    private val dbrepository:DataBaseRepository
) :DataBaseInteractor{
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