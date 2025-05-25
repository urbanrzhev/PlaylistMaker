package com.example.playlistmaker.main.ui.view_model

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.creator.Creator

class MainViewModel:ViewModel() {
    private val getStartActivityUseCase = Creator.provideGetStartActivityUseCase()
    fun startPlayerActivity():Boolean = getStartActivityUseCase.execute()
}