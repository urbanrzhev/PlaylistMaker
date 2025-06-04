package com.example.playlistmaker.search.di;

import com.example.playlistmaker.search.data.network.TracksRepositoryImpl
import com.example.playlistmaker.search.domain.api.TracksRepository
import org.koin.dsl.module

val searchRepositoryModule  = module{
    factory<TracksRepository> {
        TracksRepositoryImpl(get())
    }
}
