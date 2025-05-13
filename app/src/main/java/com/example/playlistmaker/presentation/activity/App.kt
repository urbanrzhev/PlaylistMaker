package com.example.playlistmaker.presentation.activity

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.Creator
import com.example.playlistmaker.domain.api.AppDarkThemeInteractor

class App : Application() {
    private var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        Creator.provideAppDarkThemeInteractor(this).getAppDarkTheme(AppDarkThemeInteractor.BooleanConsumer {
                darkTheme = it
                switchTheme(darkTheme)
            })
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        Creator.provideAppDarkThemeInteractor(this).setAppDarkTheme(darkTheme)
    }
}
