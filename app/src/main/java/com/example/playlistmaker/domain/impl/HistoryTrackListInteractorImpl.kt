package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.HistoryTrackListInteraсtor
import com.example.playlistmaker.domain.api.SharedPreferencesRepository
import com.example.playlistmaker.domain.models.Track

class HistoryTrackListInteractorImpl(private val sharedPreferencesRepository: SharedPreferencesRepository):HistoryTrackListInteraсtor {
    override fun addTrack(track: Track?, consumer: HistoryTrackListInteraсtor.Consumer) {
        var trackList = sharedPreferencesRepository.getSearchHistoryTrackList()
        if(track != null){
            trackList = trackList.filter {
                it.trackId != track.trackId
            } as MutableList<Track>
            trackList.add(0, track)
            if (trackList.size > 10) trackList.removeAt(10)
            sharedPreferencesRepository.setSearchHistoryTrackList(trackList)
        }
        consumer.consume(trackList)
    }

    override fun clear() {
        sharedPreferencesRepository.clearSearchHistoryTrackList()
    }

    override fun count(): Int {
        return sharedPreferencesRepository.getSearchHistoryTrackList().size
    }
}