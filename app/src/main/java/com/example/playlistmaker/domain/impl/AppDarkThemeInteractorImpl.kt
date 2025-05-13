package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.AppDarkThemeInteractor
import com.example.playlistmaker.domain.api.SharedPreferencesRepository

class AppDarkThemeInteractorImpl(private val sharedRepository: SharedPreferencesRepository):AppDarkThemeInteractor {
    override fun getAppDarkTheme(
        consumer: AppDarkThemeInteractor.BooleanConsumer
    ) {
        consumer.consume(sharedRepository.getAppDarkTheme())
    }

    override fun setAppDarkTheme(keyData: Boolean) {
        sharedRepository.setAppDarkTheme(keyData)
    }
}