package com.example.playlistmaker

import android.content.Context
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.network.TracksRepositoryImpl
import com.example.playlistmaker.data.shared_preference.SharedPreferencesRepositoryImpl
import com.example.playlistmaker.domain.api.HistoryTrackListInteraсtor
import com.example.playlistmaker.domain.api.SharedPreferencesInteractor
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.impl.HistoryTrackListInteractorImpl
import com.example.playlistmaker.domain.impl.SharedPreferencesInteractorImpl
import com.example.playlistmaker.domain.impl.TracksInteractorImpl

object Creator {
    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    fun provideSharedPreferencesInteractor(context: Context): SharedPreferencesInteractor{
        return SharedPreferencesInteractorImpl(SharedPreferencesRepositoryImpl(context))
    }

    fun provideHistoryTrackListInteractor(context: Context):HistoryTrackListInteraсtor{
        return HistoryTrackListInteractorImpl(SharedPreferencesRepositoryImpl(context))
    }
}