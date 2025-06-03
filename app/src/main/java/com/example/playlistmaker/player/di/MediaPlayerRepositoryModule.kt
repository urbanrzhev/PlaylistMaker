package com.example.playlistmaker.player.di

import android.media.MediaPlayer
import com.example.playlistmaker.player.data.MediaPlayerRepositoryImpl
import com.example.playlistmaker.player.domain.api.MediaPlayerRepository
import org.koin.dsl.module

val mediaPlayerRepositoryModule = module{
    factory<MediaPlayerRepository>{
        MediaPlayerRepositoryImpl(MediaPlayer())
    }
}