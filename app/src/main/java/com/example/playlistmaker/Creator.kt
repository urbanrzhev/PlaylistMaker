package com.example.playlistmaker

import android.content.Context
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.network.TracksRepositoryImpl
import com.example.playlistmaker.data.shared_preference.SharedPreferencesClientImpl
import com.example.playlistmaker.data.shared_preference.SharedPreferencesRepositoryImpl
import com.example.playlistmaker.domain.api.ActiveTrackForMediaPlayerInteractor
import com.example.playlistmaker.domain.api.AppDarkThemeInteractor
import com.example.playlistmaker.domain.api.HistoryTrackListInteraсtor
import com.example.playlistmaker.domain.api.SharedPreferencesRepository
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
    private fun getSharedPreferencesRepository(context: Context): SharedPreferencesRepository {
        return SharedPreferencesRepositoryImpl(SharedPreferencesClientImpl(context = context))
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    fun provideHistoryTrackListInteractor(context: Context):HistoryTrackListInteraсtor{
        return HistoryTrackListInteractorImpl(getSharedPreferencesRepository(context))
    }

    fun provideStartActivityInteractor(context: Context):StartActivityInteractor{
        return StartActivityInteractorImpl(getSharedPreferencesRepository(context))
    }

    fun provideAppDarkThemeInteractor(context: Context): AppDarkThemeInteractor {
        return AppDarkThemeInteractorImpl(getSharedPreferencesRepository(context))
    }

    fun provideActiveTrackForMediaPlayerInteractor(context: Context): ActiveTrackForMediaPlayerInteractor {
        return ActiveTrackForMediaPlayerInteractorImpl(getSharedPreferencesRepository(context))
    }
}