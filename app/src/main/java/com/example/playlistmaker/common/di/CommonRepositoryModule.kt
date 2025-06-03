package com.example.playlistmaker.common.di

import com.example.playlistmaker.common.data.repository.SharedPreferencesManagerImpl
import com.example.playlistmaker.common.domain.api.SharedPreferencesManager
import org.koin.dsl.module

val commonRepositoryModule = module{
    single<SharedPreferencesManager> {
        SharedPreferencesManagerImpl(get(),get())
    }
}