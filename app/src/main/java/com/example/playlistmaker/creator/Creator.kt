package com.example.playlistmaker.creator

import android.content.Context
import android.media.MediaPlayer
import com.example.playlistmaker.player.data.MediaPlayerRepositoryImpl
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.data.network.TracksRepositoryImpl
import com.example.playlistmaker.common.data.repository.SharedPreferencesManagerImpl
import com.example.playlistmaker.player.domain.api.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.api.MediaPlayerRepository
import com.example.playlistmaker.common.domain.api.SharedPreferencesManager
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.player.domain.impl.MediaPlayerInteractorImpl
import com.example.playlistmaker.search.domain.interactorImpl.TracksInteractorImpl
import com.example.playlistmaker.player.domain.use_case.GetActiveTrackUseCase
import com.example.playlistmaker.main.domain.use_case.GetStartActivityUseCase
import com.example.playlistmaker.common.domain.use_case.GetThemeUseCase
import com.example.playlistmaker.search.domain.use_case.SetActiveTrackUseCase
import com.example.playlistmaker.player.domain.use_case.SetStartActivityUseCase
import com.example.playlistmaker.app.domain.use_case.SetThemeUseCase
import com.example.playlistmaker.search.domain.api.HistoryInteractor
import com.example.playlistmaker.search.domain.interactorImpl.HistoryInteractorImpl
import com.example.playlistmaker.sharing.data.ExternalNavigator
import com.example.playlistmaker.sharing.domain.api.SharingInteractor
import com.example.playlistmaker.sharing.domain.impl.SharingInteractorImpl

object Creator {
    private lateinit var applicationContext:Context
    private fun getMediaPlayer() = MediaPlayer()
    fun initApplicationContext(context: Context){
        applicationContext = context
    }
    fun getAppContext():Context = applicationContext
    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(
            RetrofitNetworkClient(getAppContext())
        )
    }
    fun getPreferencesRepository(): SharedPreferencesManager {
        return SharedPreferencesManagerImpl()
    }
    private fun getMediaPlayerRepository(): MediaPlayerRepository {
        return MediaPlayerRepositoryImpl(getMediaPlayer())
    }
    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }
    fun provideSearchHistoryInteractor(): HistoryInteractor {
        return HistoryInteractorImpl(SharedPreferencesManagerImpl())
    }
    fun provideGetActiveTrackUseCase(): GetActiveTrackUseCase {
        return GetActiveTrackUseCase(getPreferencesRepository())
    }
    fun provideGetStartActivityUseCase(): GetStartActivityUseCase {
        return GetStartActivityUseCase(getPreferencesRepository())
    }
    fun provideGetThemeUseCase(): GetThemeUseCase {
        return GetThemeUseCase(
            getPreferencesRepository()
        )
    }
    fun provideSetActiveTrackUseCase(): SetActiveTrackUseCase {
        return SetActiveTrackUseCase(getPreferencesRepository())
    }
    fun provideSetStartActivityUseCase(): SetStartActivityUseCase {
        return SetStartActivityUseCase(getPreferencesRepository())
    }
    fun provideSetThemeUseCase(): SetThemeUseCase {
        return SetThemeUseCase(getPreferencesRepository())
    }
    fun provideMediaPlayerInteractor(): MediaPlayerInteractor {
        return MediaPlayerInteractorImpl(getMediaPlayerRepository())
    }
    fun provideSharingInteractor(): SharingInteractor{
        return SharingInteractorImpl(getAppContext(), ExternalNavigator(getAppContext()))
    }
}