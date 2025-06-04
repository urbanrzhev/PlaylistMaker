package com.example.playlistmaker.sharing.di

import android.content.Intent
import com.example.playlistmaker.sharing.data.ExternalNavigator
import org.koin.dsl.module

val sharingDataModule = module{
    factory<ExternalNavigator> {
        ExternalNavigator(get(),get())
    }
    factory<Intent> {
        Intent()
    }
}