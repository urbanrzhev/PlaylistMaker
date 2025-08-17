package com.example.playlistmaker.common.data.repository

import com.example.playlistmaker.common.db.converters.TrackDbConverter
import com.example.playlistmaker.common.db.dao.TrackDao
import com.example.playlistmaker.common.db.entity.TrackEntity
import com.example.playlistmaker.common.domain.api.DataBaseRepository
import com.example.playlistmaker.common.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DataBaseRepositoryImpl(
    private val db: TrackDao,
    private val converter: TrackDbConverter
) : DataBaseRepository {
    override suspend fun setFavoriteTrack(track: Track) {
            val trackEntity = converterFromTrack(track)
            db.setFavoriteTrack(trackEntity)
    }

    override suspend fun deleteFavoriteTrack(trackId: Int) {
            db.deleteFavoriteTrack(trackId)
    }

    override fun getAllFavoritesTracks(): Flow<List<Track>> {
        return flow {
            val list = db.getAllFavoritesTracks()
            emit(converterAllFromTrackEntity(list))
        }
    }

    private fun converterFromTrack(track: Track): TrackEntity {
        return converter.map(track)
    }

    private fun converterAllFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.sortedBy { track ->
            track.timeOfAddition
        }.map {
            converter.map(it)
        }.reversed()
    }
}