package com.example.playlistmaker.app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.app.domain.api.SetThemeUseCase
import com.example.playlistmaker.app.domain.di.appModule
import com.example.playlistmaker.app.domain.use_case.SetThemeUseCaseImpl
import com.example.playlistmaker.common.di.commonModule
import com.example.playlistmaker.common.domain.api.GetThemeUseCase
import com.example.playlistmaker.main.di.mainModule
import com.example.playlistmaker.media_library.di.mediaLibraryModule
import com.example.playlistmaker.player.di.mediaPlayerModule
import com.example.playlistmaker.search.di.searchModule
import com.example.playlistmaker.settings.ui.di.settingsModule
import com.example.playlistmaker.sharing.di.sharingModule
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    private var darkTheme = false
    private val themeGet: GetThemeUseCase by lazy { getKoin().get() }
    private val themeSet: SetThemeUseCase by lazy { getKoin().get() }

    override fun onCreate() {
        SetThemeUseCaseImpl
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                appModule,
                commonModule,
                mainModule,
                mediaPlayerModule,
                searchModule,
                settingsModule,
                sharingModule,
                mediaLibraryModule
            )
        }
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

