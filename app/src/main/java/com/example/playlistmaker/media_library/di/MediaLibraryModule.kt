package com.example.playlistmaker.media_library.di

import com.example.playlistmaker.media_library.domain.api.GetStartFragmentUseCase
import com.example.playlistmaker.media_library.domain.api.SetStartFragmentUseCase
import com.example.playlistmaker.media_library.domain.use_case.GetStartFragmentUseCaseImpl
import com.example.playlistmaker.media_library.domain.use_case.SetStartFragmentUseCaseImpl
import com.example.playlistmaker.media_library.ui.view_model.FavoritesTracksViewModel
import com.example.playlistmaker.media_library.ui.view_model.MediaLibraryViewModel
import com.example.playlistmaker.media_library.ui.view_model.PlaylistsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mediaLibraryModule = module{
    viewModel<MediaLibraryViewModel> {
        MediaLibraryViewModel(get(),get())
    }
    viewModel<FavoritesTracksViewModel>{
        FavoritesTracksViewModel(get())
    }
    viewModel<PlaylistsViewModel>{
        PlaylistsViewModel()
    }
    factory<SetStartFragmentUseCase>{
        SetStartFragmentUseCaseImpl(get())
    }
    factory<GetStartFragmentUseCase>{
        GetStartFragmentUseCaseImpl(get())
    }
}