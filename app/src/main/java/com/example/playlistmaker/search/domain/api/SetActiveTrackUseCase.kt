package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.common.domain.models.Track

interface SetActiveTrackUseCase {
    fun execute(track: Track)
}