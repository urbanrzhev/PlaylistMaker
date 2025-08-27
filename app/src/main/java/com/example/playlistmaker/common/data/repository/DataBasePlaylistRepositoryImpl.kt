package com.example.playlistmaker.common.data.repository

import com.example.playlistmaker.common.db.dao.PlaylistDao
import com.example.playlistmaker.common.db.entity.PlaylistEntity
import com.example.playlistmaker.common.domain.api.DataBasePlaylistsRepository
import com.example.playlistmaker.common.domain.models.Playlist
import com.example.playlistmaker.common.util.OrthographyCount
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DataBasePlaylistRepositoryImpl(
    private val database: PlaylistDao,
    private val gson: Gson,
    private val orthography: OrthographyCount
) : DataBasePlaylistsRepository {
    private val gsonList = object : TypeToken<List<Int>>() {}.getType()
    override suspend fun setPlaylist(playlist: Playlist) {
        val playlistEntity = converterFromPlaylist(playlist)
        database.setPlaylist(playlistEntity)
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return flow {
            val list = converterAllFromPlaylistEntity(database.getPlaylists())
            emit(list)
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