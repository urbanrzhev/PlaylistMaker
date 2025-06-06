package com.example.playlistmaker.settings.ui.view_model

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.app.App
import com.example.playlistmaker.common.domain.api.GetThemeUseCase
import com.example.playlistmaker.sharing.domain.api.SharingInteractor

class SettingsViewModel(
    private val app: App,
    private val getThemeUseCase: GetThemeUseCase,
    private val sharingInteractor: SharingInteractor
) : ViewModel() {
    private var errorShare = MutableLiveData<String>(null)
    fun observeShare():LiveData<String> = errorShare
    fun getTheme(): Boolean = getThemeUseCase.execute()
    fun setTheme(value: Boolean) {
        app.switchTheme(value)
    }

    fun share() {
        val error = sharingInteractor.shareApp()
        error?: errorShare.value = error
    }

    fun email() {
        sharingInteractor.openSupport()
    }

    fun terms() {
        sharingInteractor.openTerms()
    }
}
