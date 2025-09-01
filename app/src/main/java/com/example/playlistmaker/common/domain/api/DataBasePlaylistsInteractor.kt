package com.example.playlistmaker.common.domain.api

import com.example.playlistmaker.common.domain.models.Playlist
import com.example.playlistmaker.common.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface DataBasePlaylistsInteractor {
    suspend fun setPlaylist(playlist:Playlist)
    fun getPlaylists():Flow<List<Playlist>>
    fun setTrackInPlaylist(track: Track):Flow<Boolean>
    fun getTrackListInPlaylist(trackId: Int):Flow<List<Track>>
    suspend fun updatePlaylist(playlist: Playlist)
}