package com.example.playlistmaker.new_playlist.di

import com.example.playlistmaker.new_playlist.data.file.FileRepositoryImpl
import com.example.playlistmaker.new_playlist.domain.api.FileRepository
import com.example.playlistmaker.new_playlist.domain.api.FileUseCase
import com.example.playlistmaker.new_playlist.domain.impl.FileUseCaseImpl
import com.example.playlistmaker.new_playlist.ui.view_model.NewPlaylistViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val newPlaylistModule = module {
    viewModel {
        NewPlaylistViewModel(androidContext(),get(),get())
    }
    factory<FileRepository> {
        FileRepositoryImpl(get())
    }
    factory <FileUseCase>{
        FileUseCaseImpl(get())
    }
}