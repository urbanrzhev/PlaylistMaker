package com.example.playlistmaker.domain.api

interface AppDarkThemeInteractor {
    fun getAppDarkTheme(
        consumer: BooleanConsumer
    )
    fun setAppDarkTheme(keyData: Boolean)
    fun interface BooleanConsumer {
        fun consume(response: Boolean)
    }
}
