package com.example.playlistmaker.common.domain.api

import com.example.playlistmaker.common.domain.models.Playlist
import com.example.playlistmaker.common.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface DataBasePlaylistsRepository {
    suspend fun setPlaylist(playlist: Playlist)
    fun getPlaylists():Flow<List<Playlist>>
    fun getPlaylist(playlistName: String):Flow<Playlist>
    fun deletePlaylistFirstStage(playlist: Playlist):Flow<List<Int>>
    suspend fun deletePlaylistSecondStage(deleteList:List<Int>)
    fun addTrackInPlaylist(track:Track, playlistName:String):Flow<Boolean>
    fun getTracks(playlistName: String):Flow<List<Track>>
    suspend fun deleteTrackFromPlaylist(track:Track, playlistName:String)
}