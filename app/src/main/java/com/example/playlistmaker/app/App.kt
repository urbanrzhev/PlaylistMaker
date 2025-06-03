package com.example.playlistmaker.app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.app.domain.api.SetThemeUseCase
import com.example.playlistmaker.app.domain.di.appUseCaseModule
import com.example.playlistmaker.app.domain.use_case.SetThemeUseCaseImpl
import com.example.playlistmaker.common.di.commonDataModule
import com.example.playlistmaker.common.di.commonRepositoryModule
import com.example.playlistmaker.common.di.commonUseCaseModule
import com.example.playlistmaker.common.domain.api.GetThemeUseCase
import com.example.playlistmaker.main.di.mainUseCaseModule
import com.example.playlistmaker.main.di.mainViewModelModule
import com.example.playlistmaker.player.di.mediaPlayerInteractorModule
import com.example.playlistmaker.player.di.mediaPlayerRepositoryModule
import com.example.playlistmaker.player.di.mediaPlayerUseCaseModule
import com.example.playlistmaker.player.di.mediaPlayerViewModelModule
import com.example.playlistmaker.search.di.searchDataModule
import com.example.playlistmaker.search.di.searchInteractorModule
import com.example.playlistmaker.search.di.searchRepositoryModule
import com.example.playlistmaker.search.di.searchUseCaseModule
import com.example.playlistmaker.search.di.searchViewModelModule
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
                appUseCaseModule,
                commonDataModule,
                commonRepositoryModule,
                commonUseCaseModule,
                mainUseCaseModule,
                mainViewModelModule,
                mediaPlayerUseCaseModule,
                mediaPlayerInteractorModule,
                mediaPlayerRepositoryModule,
                mediaPlayerViewModelModule,
                searchDataModule,
                searchRepositoryModule,
                searchInteractorModule,
                searchViewModelModule,
                searchUseCaseModule
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

