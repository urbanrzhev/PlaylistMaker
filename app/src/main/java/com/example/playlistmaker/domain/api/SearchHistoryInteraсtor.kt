package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface SearchHistoryInteractor {
    fun addTrack(track:Track?, consumer: Consumer)
    fun clear()
    fun count():Int
    fun interface Consumer{
        fun consume(trackList:List<Track>)
    }
}