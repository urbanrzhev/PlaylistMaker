package com.example.playlistmaker.common.domain.api

import com.example.playlistmaker.common.domain.models.Playlist
import com.example.playlistmaker.common.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface DataBasePlaylistsRepository {
    suspend fun setPlaylist(playlist: Playlist)
    suspend fun updatePlaylist(playlist:Playlist)
    fun getPlaylists():Flow<List<Playlist>>
    fun getPlaylist(playlistId:Long):Flow<Playlist>
    fun deletePlaylistFirstStage(playlist: Playlist):Flow<List<Int>>
    suspend fun deletePlaylistSecondStage(deleteList:List<Int>)
    fun addTrackInPlaylist(track:Track, playlistId:Long):Flow<Boolean>
    fun getTracks(playlistId:Long):Flow<List<Track>>
    suspend fun deleteTrackFromPlaylist(track:Track, playlistId:Long)
}