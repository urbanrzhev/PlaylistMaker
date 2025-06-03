package com.example.playlistmaker.search.di

import com.example.playlistmaker.search.domain.api.HistoryInteractor
import com.example.playlistmaker.search.domain.impl.TracksInteractorImpl
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.impl.HistoryInteractorImpl
import org.koin.dsl.module
import java.util.concurrent.Executor
import java.util.concurrent.Executors

val searchInteractorModule  = module {
    single<TracksInteractor> {
        TracksInteractorImpl(get(),get())
    }
    factory<HistoryInteractor>{
        HistoryInteractorImpl(get())
    }
    factory<Executor> {
        Executors.newCachedThreadPool()
    }
}