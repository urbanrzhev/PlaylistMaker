package com.example.playlistmaker.settings.ui.di

import com.example.playlistmaker.app.App
import com.example.playlistmaker.settings.ui.view_model.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val settingsViewModelModule = module{
    viewModel {
        SettingsViewModel(androidContext() as App,get(),get())
    }
}