package com.example.playlistmaker.settings.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.app.App
import com.example.playlistmaker.common.domain.api.GetThemeUseCase
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.sharing.domain.api.SharingInteractor

class SettingsViewModel(
    private val app: App,
    private val getThemeUseCase: GetThemeUseCase,
    private val sharingInteractor: SharingInteractor
) : ViewModel() {
    fun getTheme(): Boolean = getThemeUseCase.execute()
    fun setTheme(value: Boolean) {
        app.switchTheme(value)
    }

    fun share(callback: (String?) -> Unit) {
        callback(sharingInteractor.shareApp())
    }

    fun email() {
        sharingInteractor.openSupport()
    }

    fun terms() {
        sharingInteractor.openTerms()
    }

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsViewModel(
                    app = (this[APPLICATION_KEY] as App),
                    getThemeUseCase = Creator.provideGetThemeUseCase(),
                    sharingInteractor = Creator.provideSharingInteractor()
                )
            }
        }
    }
}