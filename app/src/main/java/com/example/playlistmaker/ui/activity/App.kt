package com.example.playlistmaker.ui.activity

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.util.Creator
import com.example.playlistmaker.domain.use_case.GetThemeUseCase
import com.example.playlistmaker.domain.use_case.SetThemeUseCase

class App : Application() {
    private var darkTheme = false
    private lateinit var themeGet: GetThemeUseCase
    private lateinit var themeSet: SetThemeUseCase

    companion object{
        lateinit var appContext:Context
    }
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        themeGet = Creator.provideGetThemeUseCase()
        themeSet = Creator.provideSetThemeUseCase()
        themeGet.getTheme {
            darkTheme = it
        }
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
        themeSet.setTheme(darkTheme)
    }
}
