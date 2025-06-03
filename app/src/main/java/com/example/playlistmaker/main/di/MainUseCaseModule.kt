package com.example.playlistmaker.main.di

import com.example.playlistmaker.main.domain.api.GetStartActivityUseCase
import com.example.playlistmaker.main.domain.use_case.GetStartActivityUseCaseImpl
import org.koin.dsl.module

val mainUseCaseModule = module{
    single<GetStartActivityUseCase>{
        GetStartActivityUseCaseImpl(get())
    }
}