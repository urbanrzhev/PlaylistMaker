package com.example.playlistmaker.common.domain.impl

import com.example.playlistmaker.common.domain.api.DataBasePlaylistsInteractor
import com.example.playlistmaker.common.domain.api.DataBasePlaylistsRepository
import com.example.playlistmaker.common.domain.models.Playlist
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

    override suspend fun updatePlaylist(playlist: Playlist, trackId: Int): Boolean {
        playlist.idsTrack.forEach {
            if (it == trackId)
                return false
        }
        playlist.idsTrack.add(trackId)
        repository.setPlaylist(playlist)
        return true
    }
}