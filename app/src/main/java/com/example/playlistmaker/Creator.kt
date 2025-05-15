package com.example.playlistmaker

import android.media.MediaPlayer
import com.example.playlistmaker.data.media.MediaPlayerRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.network.TracksRepositoryImpl
import com.example.playlistmaker.data.shared_preference.SharedPreferencesManagerImpl
import com.example.playlistmaker.domain.api.MediaPlayerInteractor
import com.example.playlistmaker.domain.api.MediaPlayerRepository
import com.example.playlistmaker.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.domain.api.SharedPreferencesManager
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.impl.MediaPlayerInteractorImpl
import com.example.playlistmaker.domain.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.domain.impl.TracksInteractorImpl
import com.example.playlistmaker.domain.use_case.GetActiveTrackUseCase
import com.example.playlistmaker.domain.use_case.GetStartActivityUseCase
import com.example.playlistmaker.domain.use_case.GetThemeUseCase
import com.example.playlistmaker.domain.use_case.SetActiveTrackUseCase
import com.example.playlistmaker.domain.use_case.SetStartActivityUseCase
import com.example.playlistmaker.domain.use_case.SetThemeUseCase

object Creator {
    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }
    private fun getPreferencesRepository(): SharedPreferencesManager {
        return SharedPreferencesManagerImpl()
    }
    private fun getMediaPlayerRepository(): MediaPlayerRepository {
        return MediaPlayerRepositoryImpl(MediaPlayer())
    }
    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }
    fun provideSearchHistoryInteractor(): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(SharedPreferencesManagerImpl())
    }
    fun provideGetActiveTrackUseCase():GetActiveTrackUseCase{
        return GetActiveTrackUseCase(getPreferencesRepository())
    }
    fun provideGetStartActivityUseCase():GetStartActivityUseCase{
        return GetStartActivityUseCase(getPreferencesRepository())
    }
    fun provideGetThemeUseCase():GetThemeUseCase{
        return GetThemeUseCase(getPreferencesRepository())
    }
    fun provideSetActiveTrackUseCase():SetActiveTrackUseCase{
        return SetActiveTrackUseCase(getPreferencesRepository())
    }
    fun provideSetStartActivityUseCase():SetStartActivityUseCase{
        return SetStartActivityUseCase(getPreferencesRepository())
    }
    fun provideSetThemeUseCase():SetThemeUseCase{
        return SetThemeUseCase(getPreferencesRepository())
    }
    fun provedeMediaPlayerInteractor():MediaPlayerInteractor{
        return MediaPlayerInteractorImpl(getMediaPlayerRepository())
    }
}