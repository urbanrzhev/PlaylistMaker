package com.example.playlistmaker.search.domain.interactorImpl

import com.example.playlistmaker.common.domain.api.SharedPreferencesManager
import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.search.domain.api.HistoryInteractor

private const val KEY_HISTORY_TRACK_LIST = "key_history_track_"

class HistoryInteractorImpl(private val sharedPreferencesManager: SharedPreferencesManager):
    HistoryInteractor {
    override fun addTrack(track: Track?, consumer: HistoryInteractor.Consumer) {
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

    override fun getTracks():List<Track> {
        return sharedPreferencesManager.getTrackList(KEY_HISTORY_TRACK_LIST)
    }

    override fun clear() {
        sharedPreferencesManager.clearTrackList(KEY_HISTORY_TRACK_LIST, true)
    }

    override fun count(): Int {
        return sharedPreferencesManager.getTrackList(KEY_HISTORY_TRACK_LIST).size
    }
}
