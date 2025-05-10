package com.example.playlistmaker

import android.content.Context
import android.view.View
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.network.TracksRepositoryImpl
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.impl.TracksInteractorImpl

object Creator {
    private fun getTracksRepository(baseUrl: String): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient(baseUrl))
    }

    fun provideTracksInteractor(baseUrl:String): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository(baseUrl))
    }
}
