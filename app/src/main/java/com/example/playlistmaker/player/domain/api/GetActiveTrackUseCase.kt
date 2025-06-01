package com.example.playlistmaker.player.domain.api

import com.example.playlistmaker.common.domain.models.Track

interface GetActiveTrackUseCase {
    fun execute(): Track
}