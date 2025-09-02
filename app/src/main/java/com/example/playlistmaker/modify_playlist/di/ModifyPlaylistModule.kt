package com.example.playlistmaker.modify_playlist.di

import com.example.playlistmaker.modify_playlist.ui.view_models.ModifyPlaylistViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val modifyPlaylistModule = module{
    viewModel<ModifyPlaylistViewModel> {
        ModifyPlaylistViewModel(get())
    }
}