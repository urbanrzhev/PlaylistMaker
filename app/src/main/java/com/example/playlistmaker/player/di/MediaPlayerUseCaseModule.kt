package com.example.playlistmaker.player.di

import com.example.playlistmaker.player.domain.api.GetActiveTrackUseCase
import com.example.playlistmaker.player.domain.api.SetStartActivityUseCase
import com.example.playlistmaker.player.domain.use_case.GetActiveTrackUseCaseImpl
import com.example.playlistmaker.player.domain.use_case.SetStartActivityUseCaseImpl
import org.koin.dsl.module

val mediaPlayerUseCaseModule = module{
    single<GetActiveTrackUseCase>{
        GetActiveTrackUseCaseImpl(get())
    }
    single <SetStartActivityUseCase>{
        SetStartActivityUseCaseImpl(get())
    }
}