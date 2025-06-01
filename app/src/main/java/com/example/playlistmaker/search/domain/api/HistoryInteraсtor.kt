package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.common.domain.models.Track

interface HistoryInteractor {
    fun addTrack(track: Track?, consumer: Consumer)
    fun getTracks():List<Track>
    fun clear()
    fun count():Int
    fun interface Consumer{
        fun consume(trackList:List<Track>)
    }
}