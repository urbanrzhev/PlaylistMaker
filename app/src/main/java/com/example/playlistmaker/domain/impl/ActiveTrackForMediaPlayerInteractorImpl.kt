package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.ActiveTrackForMediaPlayerInteractor
import com.example.playlistmaker.domain.api.SharedPreferencesRepository
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.util.MyConstants

class ActiveTrackForMediaPlayerInteractorImpl(private val sharedRepository: SharedPreferencesRepository):ActiveTrackForMediaPlayerInteractor {
    private val key = MyConstants.MY_KEY_DEFAULT_TRACK_MEDIA_LIBRARY_ACTIVITY
    override fun getActiveTrackForMediaPlayer(
        consumer: ActiveTrackForMediaPlayerInteractor.TrackConsumer
    ) {
        consumer.consume(sharedRepository.getTrack(key))
    }

    override fun setActiveTrackForMediaPlayer(data: Track) {
        sharedRepository.setTrack(key,data)
    }
}