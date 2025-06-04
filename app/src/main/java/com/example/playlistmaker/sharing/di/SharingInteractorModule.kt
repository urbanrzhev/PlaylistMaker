package com.example.playlistmaker.sharing.di

import com.example.playlistmaker.sharing.domain.api.SharingInteractor
import com.example.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import org.koin.dsl.module

val sharingInteractorModule = module{
    single<SharingInteractor>{
        SharingInteractorImpl(get(),get())
    }
}