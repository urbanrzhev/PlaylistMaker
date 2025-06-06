package com.example.playlistmaker.main.di

import com.example.playlistmaker.main.domain.api.GetStartActivityUseCase
import com.example.playlistmaker.main.domain.use_case.GetStartActivityUseCaseImpl
import com.example.playlistmaker.main.ui.view_model.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainModule = module{
    viewModel {
        MainViewModel(get())
    }
    factory<GetStartActivityUseCase>{
        GetStartActivityUseCaseImpl(get())
    }
}