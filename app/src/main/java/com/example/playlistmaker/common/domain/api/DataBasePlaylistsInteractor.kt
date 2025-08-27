package com.example.playlistmaker.common.domain.api

import com.example.playlistmaker.common.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

interface DataBasePlaylistsInteractor {
    suspend fun setPlaylist(playlist:Playlist)
    fun getPlaylists():Flow<List<Playlist>>
    suspend fun updatePlaylist(playlist:Playlist,trackId:Int):Boolean
}