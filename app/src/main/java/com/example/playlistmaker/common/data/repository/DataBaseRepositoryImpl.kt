package com.example.playlistmaker.common.data.repository

import android.util.Log
import com.example.playlistmaker.common.db.AppDatabase
import com.example.playlistmaker.common.db.converters.TrackDbConverter
import com.example.playlistmaker.common.db.entity.TrackEntity
import com.example.playlistmaker.common.domain.api.DataBaseRepository
import com.example.playlistmaker.common.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class DataBaseRepositoryImpl(
    private val db: AppDatabase,
    private val converter: TrackDbConverter
) : DataBaseRepository {
    override suspend fun setFavoriteTrack(track: Track) {
        Log.v("my", "repository setFavoriteTrack")
        withContext(Dispatchers.IO) {
            val trackEntity = converterFromTrack(track)
            db.getTrackDao().setFavoriteTrack(trackEntity)
        }
    }

    override suspend fun deleteFavoriteTrack(trackId: Int) {
        withContext(Dispatchers.IO) {
            db.getTrackDao().deleteFavoriteTrack(trackId)
        }
    }

    override fun getAllFavoritesTracks(): Flow<List<Track>> {
        return flow {
            val list = db.getTrackDao().getAllFavoritesTracks()
            emit(converterAllFromTrackEntity(list))
        }
    }

    private fun converterFromTrackEntity(track: TrackEntity): Track {
        return converter.map(track)
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