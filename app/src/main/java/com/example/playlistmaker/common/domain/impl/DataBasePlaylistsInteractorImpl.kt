package com.example.playlistmaker.common.domain.impl

import com.example.playlistmaker.common.domain.api.DataBasePlaylistsInteractor
import com.example.playlistmaker.common.domain.api.DataBasePlaylistsRepository
import com.example.playlistmaker.common.domain.models.Playlist
import com.example.playlistmaker.common.domain.models.Track
import kotlinx.coroutines.flow.Flow

class DataBasePlaylistsInteractorImpl(
    private val repository: DataBasePlaylistsRepository
) : DataBasePlaylistsInteractor {
    override suspend fun setPlaylist(playlist: Playlist) {
        repository.setPlaylist(playlist = playlist)
    }

    override suspend fun updatePlaylist(playlist:Playlist) {
        repository.updatePlaylist(playlist = playlist)
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return repository.getPlaylists()
    }

    override fun getPlaylist(playlistId:Long): Flow<Playlist> {
        return repository.getPlaylist(playlistId = playlistId)
    }

    override fun deletePlaylistFirstStage(playlist: Playlist): Flow<List<Int>> {
        return repository.deletePlaylistFirstStage(playlist = playlist)
    }

    override suspend fun deletePlaylistSecondStage(deleteList: List<Int>) {
        repository.deletePlaylistSecondStage(deleteList = deleteList)
    }

    override fun addTrackInPlaylist(track: Track, playlistId:Long): Flow<Boolean> {
        return repository.addTrackInPlaylist(track, playlistId)
    }

    override fun getTracks(playlistId:Long): Flow<List<Track>> {
        return repository.getTracks(playlistId)
    }

    override suspend fun deleteTrackFromPlaylist(track: Track, playlistId:Long) {
        repository.deleteTrackFromPlaylist(track, playlistId)
    }
}