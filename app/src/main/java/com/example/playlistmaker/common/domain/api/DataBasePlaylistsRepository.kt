package com.example.playlistmaker.common.domain.api

import com.example.playlistmaker.common.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

interface DataBasePlaylistsRepository {
    suspend fun setPlaylist(playlist: Playlist)
    fun getPlaylists():Flow<List<Playlist>>
}