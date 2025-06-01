package com.example.playlistmaker.main.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.main.domain.api.GetStartActivityUseCase

class MainViewModel(
    private val getStartActivityUseCase: GetStartActivityUseCase
) : ViewModel() {
    fun startPlayerActivity(): Boolean = getStartActivityUseCase.execute()

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                MainViewModel(
                    getStartActivityUseCase = Creator.provideGetStartActivityUseCase()
                )
            }
        }
    }
}