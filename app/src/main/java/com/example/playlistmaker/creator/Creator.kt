package com.example.playlistmaker.creator

import android.content.Context
import android.media.MediaPlayer
import com.example.playlistmaker.app.domain.api.SetThemeUseCase
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
import com.example.playlistmaker.player.domain.use_case.GetActiveTrackUseCaseImpl
import com.example.playlistmaker.main.domain.use_case.GetStartActivityUseCaseImpl
import com.example.playlistmaker.common.domain.use_case.GetThemeUseCaseImpl
import com.example.playlistmaker.player.domain.use_case.SetStartActivityUseCaseImpl
import com.example.playlistmaker.app.domain.use_case.SetThemeUseCaseImpl
import com.example.playlistmaker.common.domain.api.GetThemeUseCase
import com.example.playlistmaker.main.domain.api.GetStartActivityUseCase
import com.example.playlistmaker.player.domain.api.GetActiveTrackUseCase
import com.example.playlistmaker.player.domain.api.SetStartActivityUseCase
import com.example.playlistmaker.search.domain.api.HistoryInteractor
import com.example.playlistmaker.search.domain.api.SetActiveTrackUseCase
import com.example.playlistmaker.search.domain.interactorImpl.HistoryInteractorImpl
import com.example.playlistmaker.search.domain.use_case.SetActiveTrackUseCaseImpl
import com.example.playlistmaker.sharing.data.ExternalNavigator
import com.example.playlistmaker.sharing.domain.api.SharingInteractor
import com.example.playlistmaker.sharing.domain.impl.SharingInteractorImpl

object Creator {
    private lateinit var applicationContext: Context
    private fun getMediaPlayer() = MediaPlayer()
    fun initApplicationContext(context: Context) {
        applicationContext = context
    }

    fun getAppContext(): Context = applicationContext
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
        return GetActiveTrackUseCaseImpl(getPreferencesRepository())
    }

    fun provideGetStartActivityUseCase(): GetStartActivityUseCase {
        return GetStartActivityUseCaseImpl(getPreferencesRepository())
    }

    fun provideGetThemeUseCase(): GetThemeUseCase {
        return GetThemeUseCaseImpl(
            getPreferencesRepository()
        )
    }

    fun provideSetActiveTrackUseCase(): SetActiveTrackUseCase {
        return SetActiveTrackUseCaseImpl(getPreferencesRepository())
    }

    fun provideSetStartActivityUseCase(): SetStartActivityUseCase {
        return SetStartActivityUseCaseImpl(getPreferencesRepository())
    }

    fun provideSetThemeUseCase(): SetThemeUseCase {
        return SetThemeUseCaseImpl(getPreferencesRepository())
    }

    fun provideMediaPlayerInteractor(): MediaPlayerInteractor {
        return MediaPlayerInteractorImpl(getMediaPlayerRepository())
    }

    fun provideSharingInteractor(): SharingInteractor {
        return SharingInteractorImpl(getAppContext(), ExternalNavigator(getAppContext()))
    }
}