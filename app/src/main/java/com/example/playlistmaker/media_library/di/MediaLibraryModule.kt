package com.example.playlistmaker.media_library.di

import com.example.playlistmaker.media_library.ui.view_model.FavoritesTracksViewModel
import com.example.playlistmaker.media_library.ui.view_model.PlaylistsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mediaLibraryModule = module{
    viewModel<FavoritesTracksViewModel>{
        FavoritesTracksViewModel()
    }
    viewModel<PlaylistsViewModel>{
        PlaylistsViewModel()
    }
}