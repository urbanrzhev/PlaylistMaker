package com.example.playlistmaker.new_playlist.di

import com.example.playlistmaker.new_playlist.ui.view_model.NewPlaylistViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val newPlaylistModule = module {
    viewModel {
        NewPlaylistViewModel()
    }
}