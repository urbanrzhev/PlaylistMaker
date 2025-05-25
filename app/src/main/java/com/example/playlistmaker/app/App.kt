package com.example.playlistmaker.app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.common.domain.use_case.GetThemeUseCase
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.app.domain.use_case.SetThemeUseCase

class App : Application() {
    private var darkTheme = false
    private lateinit var themeGet: GetThemeUseCase
    private lateinit var themeSet: SetThemeUseCase

    override fun onCreate() {
        super.onCreate()
        Creator.initApplicationContext(this)
        themeGet = Creator.provideGetThemeUseCase()
        themeSet = Creator.provideSetThemeUseCase()
        darkTheme = themeGet.execute()
        switchTheme((darkTheme))
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
        themeSet.execute(darkTheme)
    }
}
