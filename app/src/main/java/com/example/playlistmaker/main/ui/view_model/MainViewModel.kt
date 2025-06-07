package com.example.playlistmaker.main.ui.view_model

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.main.domain.api.GetStartActivityUseCase

class MainViewModel(
    private val getStartActivityUseCase: GetStartActivityUseCase
) : ViewModel() {
    fun startPlayerActivity(): Boolean = getStartActivityUseCase.execute()
}