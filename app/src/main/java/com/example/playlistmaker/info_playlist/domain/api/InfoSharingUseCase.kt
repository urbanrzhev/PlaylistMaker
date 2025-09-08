package com.example.playlistmaker.info_playlist.domain.api

import com.example.playlistmaker.common.domain.models.Playlist
import com.example.playlistmaker.common.domain.models.Track

interface InfoSharingUseCase {
    fun execute(playlist: Playlist,tracks:List<Track>):String?
}