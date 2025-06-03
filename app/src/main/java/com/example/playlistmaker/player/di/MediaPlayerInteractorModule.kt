package com.example.playlistmaker.player.di

import com.example.playlistmaker.player.domain.api.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.impl.MediaPlayerInteractorImpl
import org.koin.dsl.module

val mediaPlayerInteractorModule = module{
    factory<MediaPlayerInteractor>{
        MediaPlayerInteractorImpl(get())
    }
}