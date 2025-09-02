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

    override fun getPlaylists(): Flow<List<Playlist>> {
        return repository.getPlaylists()
    }

    override fun getPlaylist(playlistName: String): Flow<Playlist> {
        return repository.getPlaylist(playlistName)
    }

    override fun deletePlaylistFirstStage(playlist: Playlist): Flow<List<Int>> {
        return repository.deletePlaylistFirstStage(playlist = playlist)
    }

    override suspend fun deletePlaylistSecondStage(deleteList: List<Int>) {
        repository.deletePlaylistSecondStage(deleteList = deleteList)
    }

    override fun addTrackInPlaylist(track: Track, playlistName:String): Flow<Boolean> {
        return repository.addTrackInPlaylist(track, playlistName)
    }

    override fun getTracks(playlistName: String): Flow<List<Track>> {
        return repository.getTracks(playlistName)
    }

    override suspend fun deleteTrackFromPlaylist(track: Track, playlistName: String) {
        repository.deleteTrackFromPlaylist(track, playlistName)
    }
}