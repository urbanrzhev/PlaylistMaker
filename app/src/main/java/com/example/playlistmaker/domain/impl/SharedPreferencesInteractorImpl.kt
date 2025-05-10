package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.SharedPreferencesRepository
import com.example.playlistmaker.domain.api.SharedPreferencesInteractor
import com.example.playlistmaker.domain.models.Track

class SharedPreferencesInteractorImpl(private val sharedRepository: SharedPreferencesRepository) :
    SharedPreferencesInteractor {
    override fun getStartActivity(
        consumer: SharedPreferencesInteractor.BooleanConsumer
    ) {
        consumer.consume(sharedRepository.getMediaPlayerLoadStartActivity())
    }

    override fun setStartActivity(keyData: Boolean) {
        sharedRepository.setMediaPlayerLoadStartActivity(keyData)
    }

    override fun getAppDarkTheme(
        consumer: SharedPreferencesInteractor.BooleanConsumer
    ) {
        consumer.consume(sharedRepository.getAppDarkTheme())
    }

    override fun setAppDarkTheme(keyData: Boolean) {
        sharedRepository.setAppDarkTheme(keyData)
    }

    override fun getActiveTrackForMediaPlayer(
        consumer: SharedPreferencesInteractor.TrackConsumer
    ) {
        consumer.consume(sharedRepository.getActiveTrackForMediaPlayer())
    }

    override fun setActiveTrackForMediaPlayer(keyData: Track) {
        sharedRepository.setActiveTrackForMediaPlayer(keyData)
    }

    override fun getSearchHistoryTrackList(
        consumer: SharedPreferencesInteractor.TrackListConsumer
    ) {
        consumer.consume(sharedRepository.getSearchHistoryTrackList())
    }

    override fun setSearchHistoryTrackList(trackList: MutableList<Track>) {
        sharedRepository.setSearchHistoryTrackList(trackList)
    }

    override fun clearSearchHistoryTrackList() {
        sharedRepository.clearSearchHistoryTrackList()
    }
}