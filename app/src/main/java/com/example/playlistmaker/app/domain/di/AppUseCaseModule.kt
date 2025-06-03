package com.example.playlistmaker.app.domain.di

import com.example.playlistmaker.app.domain.api.SetThemeUseCase
import com.example.playlistmaker.app.domain.use_case.SetThemeUseCaseImpl
import org.koin.dsl.module

val appUseCaseModule = module{
    single<SetThemeUseCase> {
        SetThemeUseCaseImpl(get())
    }
}