package com.example.playlistmaker.domain.impl


import com.example.playlistmaker.domain.api.AppDarkThemeInteractor
import com.example.playlistmaker.domain.api.SharedPreferencesRepository
import com.example.playlistmaker.domain.util.MyConstants

class AppDarkThemeInteractorImpl(private val sharedRepository: SharedPreferencesRepository):AppDarkThemeInteractor {
    override fun getAppDarkTheme(
        consumer: AppDarkThemeInteractor.BooleanConsumer
    ) {
        consumer.consume(sharedRepository.getBoolean(MyConstants.MY_KEY_SWITCHER))
    }

    override fun setAppDarkTheme(data: Boolean) {
        sharedRepository.setBoolean(MyConstants.MY_KEY_SWITCHER, data)
    }
}

