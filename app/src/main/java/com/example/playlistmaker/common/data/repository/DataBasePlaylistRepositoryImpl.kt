package com.example.playlistmaker.common.data.repository

import com.example.playlistmaker.common.db.converters.TrackInPlaylistEntityDbConverter
import com.example.playlistmaker.common.db.dao.PlaylistDao
import com.example.playlistmaker.common.db.dao.TrackInPlaylistsDao
import com.example.playlistmaker.common.db.entity.PlaylistEntity
import com.example.playlistmaker.common.domain.api.DataBasePlaylistsRepository
import com.example.playlistmaker.common.domain.models.Playlist
import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.common.util.OrthographyCount
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DataBasePlaylistRepositoryImpl(
    private val databaseTracks: TrackInPlaylistsDao,
    private val databasePlaylists: PlaylistDao,
    private val gson: Gson,
    private val orthography: OrthographyCount,
    private val converterTrackInPlaylistEntity: TrackInPlaylistEntityDbConverter
) : DataBasePlaylistsRepository {
    private val gsonList = object : TypeToken<List<Int>>() {}.getType()
    override suspend fun setPlaylist(playlist: Playlist) {
        val playlistEntity = converterFromPlaylist(playlist)
        databasePlaylists.setPlaylist(playlistEntity)
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return flow {
            val list = converterAllFromPlaylistEntity(databasePlaylists.getPlaylists())
            emit(list)
        }
    }

    override fun setTrackInPlaylist(track: Track): Flow<Boolean> {
        return flow {
            val trackEntity = converterTrackInPlaylistEntity.map(track)
            databaseTracks.setTrack(trackEntity)
            emit(true)
        }
    }

    private fun converterFromPlaylist(playlist: Playlist): PlaylistEntity {
        return with(playlist) {
            PlaylistEntity(
                name = name,
                photo = photo,
                description = description,
                idsTracks = gson.toJson(idsTrack)
            )
        }
    }

    private fun converterAllFromPlaylistEntity(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map {
            with(it) {
                Playlist(
                    name = name,
                    photo = photo,
                    description = description,
                    idsTrack = gson.fromJson(idsTracks, gsonList)
                ).apply {
                    count = orthography.orthographyCount(idsTrack.size)
                }
            }
        }
    }
}