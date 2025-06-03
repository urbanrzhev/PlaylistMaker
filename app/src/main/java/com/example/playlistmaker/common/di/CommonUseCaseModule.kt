package com.example.playlistmaker.common.di

import com.example.playlistmaker.common.domain.api.GetThemeUseCase
import com.example.playlistmaker.common.domain.use_case.GetThemeUseCaseImpl
import org.koin.dsl.module

val commonUseCaseModule = module{
    single <GetThemeUseCase>{
        GetThemeUseCaseImpl(get())
    }
}