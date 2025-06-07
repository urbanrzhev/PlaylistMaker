package com.example.playlistmaker.player.di

import android.media.MediaPlayer
import com.example.playlistmaker.player.data.MediaPlayerRepositoryImpl
import com.example.playlistmaker.player.domain.api.GetActiveTrackUseCase
import com.example.playlistmaker.player.domain.api.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.api.MediaPlayerRepository
import com.example.playlistmaker.player.domain.api.SetStartActivityUseCase
import com.example.playlistmaker.player.domain.impl.MediaPlayerInteractorImpl
import com.example.playlistmaker.player.domain.use_case.GetActiveTrackUseCaseImpl
import com.example.playlistmaker.player.domain.use_case.SetStartActivityUseCaseImpl
import com.example.playlistmaker.player.ui.view_model.MediaPlayerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mediaPlayerModule = module{
    viewModel {
        MediaPlayerViewModel(get(),get(),get())
    }
    factory<MediaPlayerRepository>{
        MediaPlayerRepositoryImpl(MediaPlayer())
    }
    factory<MediaPlayerInteractor>{
        MediaPlayerInteractorImpl(get())
    }
    factory<GetActiveTrackUseCase>{
        GetActiveTrackUseCaseImpl(get())
    }
    factory <SetStartActivityUseCase>{
        SetStartActivityUseCaseImpl(get())
    }
}