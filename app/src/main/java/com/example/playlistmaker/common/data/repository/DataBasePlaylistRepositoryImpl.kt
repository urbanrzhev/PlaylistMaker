package com.example.playlistmaker.common.data.repository

import com.example.playlistmaker.common.data.db.converters.TrackEntityForPlaylistDbConverter
import com.example.playlistmaker.common.data.db.dao.PlaylistDao
import com.example.playlistmaker.common.data.db.entity.CrossTrackAndPlaylistEntity
import com.example.playlistmaker.common.data.db.entity.PlaylistEntity
import com.example.playlistmaker.common.domain.api.DataBasePlaylistsRepository
import com.example.playlistmaker.common.domain.models.Playlist
import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.common.util.OrthographyCount
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DataBasePlaylistRepositoryImpl(
    private val databasePlaylists: PlaylistDao,
    private val orthography: OrthographyCount,
    private val converterTrackEntityForPlaylist: TrackEntityForPlaylistDbConverter
) : DataBasePlaylistsRepository {
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

    override fun addTrackInPlaylist(track: Track, playlistName: String): Flow<Boolean> {
        return flow {
            val trackEntityForPlaylist = converterTrackEntityForPlaylist.map(track)
            databasePlaylists.addTrackInPlaylist(
                track = trackEntityForPlaylist,
                crossEntity = CrossTrackAndPlaylistEntity(
                    trackId = track.trackId,
                    playlistName = playlistName
                )
            )
            emit(true)
        }
    }

    private fun converterFromPlaylist(playlist: Playlist): PlaylistEntity {
        return with(playlist) {
            PlaylistEntity(
                name = name,
                photo = photo,
                description = description
            )
        }
    }

    private suspend fun converterAllFromPlaylistEntity(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map {
            with(it) {
                val idsTrack = databasePlaylists.getIdsTrackFromPlaylist(it.name).toMutableList()
                Playlist(
                    name = name,
                    photo = photo,
                    description = description,
                    idsTrack = idsTrack
                ).apply {
                    count = orthography.orthographyCount(idsTrack.size)
                }
            }
        }
    }
}