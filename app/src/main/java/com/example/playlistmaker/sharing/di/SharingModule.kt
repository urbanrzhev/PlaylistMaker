package com.example.playlistmaker.sharing.di

import android.content.Intent
import com.example.playlistmaker.sharing.data.ExternalNavigator
import com.example.playlistmaker.sharing.domain.api.SharingInteractor
import com.example.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import org.koin.dsl.module

val sharingModule = module{
    factory<ExternalNavigator> {
        ExternalNavigator(get(),get())
    }
    factory<Intent> {
        Intent()
    }
    factory<SharingInteractor>{
        SharingInteractorImpl(get(),get())
    }
}