package com.example.playlistmaker.search.di

import com.example.playlistmaker.search.data.network.ITunesApiService
import com.example.playlistmaker.search.data.network.NetworkClient
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.data.network.TracksRepositoryImpl
import com.example.playlistmaker.search.domain.api.HistoryInteractor
import com.example.playlistmaker.search.domain.api.SetActiveTrackUseCase
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.impl.HistoryInteractorImpl
import com.example.playlistmaker.search.domain.impl.TracksInteractorImpl
import com.example.playlistmaker.search.domain.use_case.SetActiveTrackUseCaseImpl
import com.example.playlistmaker.search.ui.view_model.SearchViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executor
import java.util.concurrent.Executors

val searchModule = module {
    viewModel{
        SearchViewModel(get(),get(),get())
    }
    factory<TracksRepository> {
        TracksRepositoryImpl(get())
    }
    factory<SetActiveTrackUseCase>{
        SetActiveTrackUseCaseImpl(get())
    }
    factory<NetworkClient>{
        RetrofitNetworkClient(get(),androidContext())
    }
    factory<ITunesApiService> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesApiService::class.java)
    }
    factory<TracksInteractor> {
        TracksInteractorImpl(get(),get())
    }
    factory<HistoryInteractor>{
        HistoryInteractorImpl(get())
    }
    factory<Executor> {
        Executors.newCachedThreadPool()
    }
}