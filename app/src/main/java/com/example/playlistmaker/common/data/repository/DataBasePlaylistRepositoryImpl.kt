package com.example.playlistmaker.common.data.repository

import com.example.playlistmaker.common.data.db.converters.TrackEntityForPlaylistDbConverter
import com.example.playlistmaker.common.data.db.dao.PlaylistDao
import com.example.playlistmaker.common.data.db.entity.CrossTrackAndPlaylistEntity
import com.example.playlistmaker.common.data.db.entity.PlaylistEntity
import com.example.playlistmaker.common.data.db.entity.TrackEntityForPlaylist
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

    override suspend fun updatePlaylist(playlist: Playlist) {
        val playlistEntity = converterFromPlaylist(playlist)
        databasePlaylists.updatePlaylist(playlist = playlistEntity)
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return flow {
            val list = converterAllFromPlaylistEntity(databasePlaylists.getPlaylists())
            emit(list)
        }
    }

    override fun getPlaylist(playlistId: Long): Flow<Playlist> {
        return flow {
            val playlist =
                converterFromPlaylistEntity(databasePlaylists.getPlaylist(playlistId = playlistId))
            emit(playlist)
        }
    }

    override fun deletePlaylistFirstStage(playlist: Playlist): Flow<List<Int>> {
        return flow {
            val playlistEntity = converterFromPlaylist(playlist)
            val idsList =
                databasePlaylists.getIdsTrackFromPlaylist(playlistId = playlist.playlistId)
            databasePlaylists.deletePlaylist(playlistEntity)
            val resultList = getSortedMapIdList(idsList)
            emit(resultList)
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

    override fun getTracks(playlistId: Long): Flow<List<Track>> {
        return flow {
            val crossList = databasePlaylists.getIdsTrackFromPlaylist(playlistId = playlistId)
            val idsList = getSortedMapIdList(crossList)
            val tracks = databasePlaylists.getTracksFromPlaylistById(idsList)
            val sortedTracks = reversedTracks(idsList, tracks)
            val resultList = converterTrackEntityForPlaylist.map(sortedTracks)
            emit(resultList)
        }
    }

    override suspend fun deleteTrackFromPlaylist(track: Track, playlistId: Long) {
        val newTrack = converterTrackEntityForPlaylist.map(track)
        databasePlaylists.deleteTrackFromPlaylistTransaction(track = newTrack, playlistId)
    }

    private suspend fun getIdsTrackFromPlaylist(playlistId: Long): List<Int> {
        val idsList = databasePlaylists.getIdsTrackFromPlaylist(playlistId = playlistId)
        return getSortedMapIdList(idsList)
    }

    private fun getSortedMapIdList(idsList: List<CrossTrackAndPlaylistEntity>): List<Int> {
        if (idsList.isNotEmpty()) {
            val newIdsList = idsList.sortedBy {
                it.crossReferencesId
            }.map {
                it.trackId
            }
            return newIdsList
        }
        return listOf()
    }

    private fun reversedTracks(
        idsList: List<Int>,
        tracksEntityList: List<TrackEntityForPlaylist>
    ): List<TrackEntityForPlaylist> {
        if (idsList.isNotEmpty()) {
            val resultList = idsList.map { id ->
                tracksEntityList.forEach {
                    if (it.trackId == id)
                        return@map it
                }
                return@map tracksEntityList[0]
            }
            return resultList.reversed()
        }
        return listOf()
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