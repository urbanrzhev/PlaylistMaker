package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.ActiveTrackForMediaPlayerInteractor
import com.example.playlistmaker.domain.api.SharedPreferencesRepository
import com.example.playlistmaker.domain.models.Track

class ActiveTrackForMediaPlayerInteractorImpl(private val sharedRepository:SharedPreferencesRepository):ActiveTrackForMediaPlayerInteractor {
    override fun getActiveTrackForMediaPlayer(
        consumer: ActiveTrackForMediaPlayerInteractor.TrackConsumer
    ) {
        consumer.consume(sharedRepository.getActiveTrackForMediaPlayer())
    }

    override fun setActiveTrackForMediaPlayer(keyData: Track) {
        sharedRepository.setActiveTrackForMediaPlayer(keyData)
    }
}