package com.example.playlistmaker.search.di

import com.example.playlistmaker.search.domain.api.SetActiveTrackUseCase
import com.example.playlistmaker.search.domain.use_case.SetActiveTrackUseCaseImpl
import org.koin.dsl.module

val searchUseCaseModule = module {
    single<SetActiveTrackUseCase>{
        SetActiveTrackUseCaseImpl(get())
    }
}