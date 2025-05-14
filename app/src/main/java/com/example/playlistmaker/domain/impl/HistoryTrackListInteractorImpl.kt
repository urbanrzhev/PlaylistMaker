package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.HistoryTrackListInteraсtor
import com.example.playlistmaker.domain.api.SharedPreferencesRepository
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.util.MyConstants

class HistoryTrackListInteractorImpl(private val sharedPreferencesRepository: SharedPreferencesRepository):HistoryTrackListInteraсtor {
    private val key = MyConstants.KEY_HISTORY_TRACK_LIST
    override fun addTrack(track: Track?, consumer: HistoryTrackListInteraсtor.Consumer) {
        var trackList = sharedPreferencesRepository.getTrackList(key)
        if(track != null){
            trackList = trackList.filter {
                it.trackId != track.trackId
            } as MutableList<Track>
            trackList.add(0, track)
            if (trackList.size > 10) trackList.removeAt(10)
            sharedPreferencesRepository.setTrackList(key, trackList)
        }
        consumer.consume(trackList)
    }

    override fun clear() {
        sharedPreferencesRepository.clearTrackList(key, true)
    }

    override fun count(): Int {
        return sharedPreferencesRepository.getTrackList(key).size
    }
}
