package com.example.playlistmaker.info_playlist.di

import com.example.playlistmaker.info_playlist.domain.api.InfoSharingUseCase
import com.example.playlistmaker.info_playlist.domain.impl.InfoSharingUseCaseImpl
import com.example.playlistmaker.info_playlist.ui.view_model.InfoPlaylistViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val infoPlaylistModule = module{
    viewModel {
        InfoPlaylistViewModel(get(),get(),get(),get())
    }
    factory<InfoSharingUseCase> {
        InfoSharingUseCaseImpl(get(),get())
    }
}