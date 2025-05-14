package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.domain.api.SharedPreferencesManager
import com.example.playlistmaker.domain.models.Track

private const val KEY_HISTORY_TRACK_LIST = "key_history_track_"

class SearchHistoryInteractorImpl(private val sharedPreferencesManager: SharedPreferencesManager):
    SearchHistoryInteractor {
    override fun addTrack(track: Track?, consumer: SearchHistoryInteractor.Consumer) {
        var trackList = sharedPreferencesManager.getTrackList(KEY_HISTORY_TRACK_LIST)
        if(track != null){
            trackList = trackList.filter {
                it.trackId != track.trackId
            } as MutableList<Track>
            trackList.add(0, track)
            if (trackList.size > 10) trackList.removeAt(10)
            sharedPreferencesManager.setTrackList(KEY_HISTORY_TRACK_LIST, trackList)
        }
        consumer.consume(trackList)
    }

    override fun clear() {
        sharedPreferencesManager.clearTrackList(KEY_HISTORY_TRACK_LIST, true)
    }

    override fun count(): Int {
        return sharedPreferencesManager.getTrackList(KEY_HISTORY_TRACK_LIST).size
    }
}
