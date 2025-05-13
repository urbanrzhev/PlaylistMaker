package com.example.playlistmaker

import android.content.Context
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.network.TracksRepositoryImpl
import com.example.playlistmaker.data.shared_preference.SharedPreferencesRepositoryImpl
import com.example.playlistmaker.domain.api.ActiveTrackForMediaPlayerInteractor
import com.example.playlistmaker.domain.api.AppDarkThemeInteractor
import com.example.playlistmaker.domain.api.HistoryTrackListInteraсtor
import com.example.playlistmaker.domain.api.StartActivityInteractor
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.impl.ActiveTrackForMediaPlayerInteractorImpl
import com.example.playlistmaker.domain.impl.AppDarkThemeInteractorImpl
import com.example.playlistmaker.domain.impl.HistoryTrackListInteractorImpl
import com.example.playlistmaker.domain.impl.StartActivityInteractorImpl
import com.example.playlistmaker.domain.impl.TracksInteractorImpl

object Creator {
    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    fun provideHistoryTrackListInteractor(context: Context):HistoryTrackListInteraсtor{
        return HistoryTrackListInteractorImpl(SharedPreferencesRepositoryImpl(context))
    }

    fun provideStartActivityInteractor(context: Context):StartActivityInteractor{
        return StartActivityInteractorImpl(SharedPreferencesRepositoryImpl(context))
    }

    fun provideAppDarkThemeInteractor(context: Context): AppDarkThemeInteractor {
        return AppDarkThemeInteractorImpl(SharedPreferencesRepositoryImpl(context))
    }

    fun provideActiveTrackForMediaPlayerInteractor(context: Context): ActiveTrackForMediaPlayerInteractor {
        return ActiveTrackForMediaPlayerInteractorImpl(SharedPreferencesRepositoryImpl(context))
    }
}