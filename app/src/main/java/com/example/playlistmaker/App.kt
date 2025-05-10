package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.domain.api.SharedPreferencesInteractor

class App : Application() {
    private var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        Creator.provideSharedPreferencesInteractor(this).getAppDarkTheme(SharedPreferencesInteractor.BooleanConsumer {
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
        Creator.provideSharedPreferencesInteractor(this).setAppDarkTheme(darkTheme)
    }
}
