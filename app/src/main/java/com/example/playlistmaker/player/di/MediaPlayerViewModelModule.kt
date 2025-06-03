package com.example.playlistmaker.player.di

import com.example.playlistmaker.player.ui.view_model.MediaPlayerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mediaPlayerViewModelModule = module{
    viewModel {
        MediaPlayerViewModel(get(),get(),get())
    }
}