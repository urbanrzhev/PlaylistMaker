package com.example.playlistmaker.settings.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.app.App
import com.example.playlistmaker.creator.Creator

class SettingsViewModel(
    private val app:App
):ViewModel() {
    private val getThemeUseCase = Creator.provideGetThemeUseCase()
    private val sharingInteractor = Creator.provideSharingInteractor()
    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsViewModel(
                    app = (this[APPLICATION_KEY] as App),
                )
            }
        }
    }
    fun getTheme():Boolean = getThemeUseCase.execute()
    fun setTheme(value:Boolean){
        app.switchTheme(value)
    }
    fun share(callback:(String?)->Unit){
        callback(sharingInteractor.shareApp())
    }
    fun email(){
        sharingInteractor.openSupport()
    }
    fun terms(){
        sharingInteractor.openTerms()
    }
}