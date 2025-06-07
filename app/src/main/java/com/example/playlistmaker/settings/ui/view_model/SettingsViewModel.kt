package com.example.playlistmaker.settings.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.app.App
import com.example.playlistmaker.common.domain.api.GetThemeUseCase
import com.example.playlistmaker.sharing.domain.api.SharingInteractor

class SettingsViewModel(
    private val app: App,
    private val getThemeUseCase: GetThemeUseCase,
    private val sharingInteractor: SharingInteractor
) : ViewModel() {
    private var errors = MutableLiveData<String?>(null)
    fun observeErrors(): LiveData<String?> = errors
    fun getTheme(): Boolean = getThemeUseCase.execute()
    fun setTheme(value: Boolean) {
        app.switchTheme(value)
    }

    fun share() {
        errorMessage(sharingInteractor.shareApp())
    }

    fun email() {
        errorMessage(sharingInteractor.openEmail())
    }

    fun terms() {
        errorMessage(sharingInteractor.openTerms())
    }

    private fun errorMessage(error:String?){
        if(error!=null)
            errors.value = error
    }
}
