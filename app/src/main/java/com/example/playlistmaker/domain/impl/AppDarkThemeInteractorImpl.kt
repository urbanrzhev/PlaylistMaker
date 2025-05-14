package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.AppDarkThemeInteractor
import com.example.playlistmaker.domain.api.SharedPreferencesRepository

class AppDarkThemeInteractorImpl(private val sharedRepository: SharedPreferencesRepository):AppDarkThemeInteractor {
   val key = 
    override fun getAppDarkTheme(
        consumer: AppDarkThemeInteractor.BooleanConsumer
    ) {
        consumer.consume(sharedRepository.getBoolean())
    }

    override fun setAppDarkTheme(data: Boolean) {
        sharedRepository.setBoolean(key, data)
    }
}
