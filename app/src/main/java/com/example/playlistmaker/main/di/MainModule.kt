package com.example.playlistmaker.main.di

import com.example.playlistmaker.common.domain.api.SetStartActivityUseCase
import com.example.playlistmaker.common.domain.use_case.SetStartActivityUseCaseImpl
import com.example.playlistmaker.main.ui.view_model.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainModule = module{
    viewModel {
        MainViewModel(androidContext(),get(),get())
    }
    factory<SetStartActivityUseCase>{
        SetStartActivityUseCaseImpl(get())
    }
}