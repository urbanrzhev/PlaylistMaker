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

    override suspend fun updatePlaylist(playlist:Playlist) {
        val playlistEntity = converterFromPlaylist(playlist)
        databasePlaylists.updatePlaylist(playlist = playlistEntity)
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return flow {
            val list = converterAllFromPlaylistEntity(databasePlaylists.getPlaylists())
            emit(list)
        }
    }

    override fun getPlaylist(playlistId:Long): Flow<Playlist> {
        return flow {
            val playlist = converterFromPlaylistEntity(databasePlaylists.getPlaylist(playlistId = playlistId))
            emit(playlist)
        }
    }

    override fun deletePlaylistFirstStage(playlist: Playlist): Flow<List<Int>> {
        return flow {
            val playlistEntity = converterFromPlaylist(playlist)
            val deleteIdsList =
                databasePlaylists.deletePlaylistFirstStageTransaction(playlist = playlistEntity)
            emit(deleteIdsList)
        }
    }

    override suspend fun deletePlaylistSecondStage(deleteList: List<Int>) {
        databasePlaylists.deletePlaylistSecondStageTransaction(deleteIdsList = deleteList)
    }

    override fun addTrackInPlaylist(track: Track, playlistId: Long): Flow<Boolean> {
        return flow {
            val trackEntityForPlaylist = converterTrackEntityForPlaylist.map(track)
            databasePlaylists.addTrackInPlaylistTransaction(
                track = trackEntityForPlaylist,
                crossEntity = CrossTrackAndPlaylistEntity(
                    trackId = track.trackId,
                    playlistId = playlistId
                )
            )
            emit(true)
        }
    }

    override fun getTracks(playlistId:Long): Flow<List<Track>> {
        return flow {
            val listDb = databasePlaylists.getTracksFromPlaylist(playlistId)
            val newList = converterTrackEntityForPlaylist.map(listDb)
            emit(newList)
        }
    }

    override suspend fun deleteTrackFromPlaylist(track: Track, playlistId:Long) {
        val newTrack = converterTrackEntityForPlaylist.map(track)
        databasePlaylists.deleteTrackFromPlaylistTransaction(track = newTrack, playlistId)
    }

    private suspend fun getIdsTrackFromPlaylist(playlistId: Long): List<Int> {
        return databasePlaylists.getIdsTrackFromPlaylist(playlistId = playlistId)
    }

    private fun converterFromPlaylist(playlist: Playlist): PlaylistEntity {
        return with(playlist) {
            PlaylistEntity(
                playlistId = playlistId,
                name = name,
                photo = photo,
                description = description
            )
        }
    }

    private fun converterFromPlaylistEntity(playlist: PlaylistEntity): Playlist {
        return with(playlist) {
            Playlist(
                playlistId = playlistId,
                name = name,
                photo = photo,
                description = description
            )
        }
    }

    private suspend fun converterAllFromPlaylistEntity(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map {
            with(it) {
                val idsTrack = getIdsTrackFromPlaylist(it.playlistId).toMutableList()
                Playlist(
                    playlistId = playlistId,
                    name = name,
                    photo = photo,
                    description = description,
                    idsTrack = idsTrack
                ).apply {
                    count = orthography.orthographyCountTracks(idsTrack.size)
                }
            }
        }
    }
}