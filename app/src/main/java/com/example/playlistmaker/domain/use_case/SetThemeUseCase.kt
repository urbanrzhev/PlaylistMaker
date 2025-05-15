package com.example.playlistmaker.domain.use_case

import com.example.playlistmaker.domain.api.SharedPreferencesManager

private const val MY_KEY_SWITCHER = "my_key_switcher"
class SetThemeUseCase(private val sharedPreferencesManager: SharedPreferencesManager) {
    fun setTheme(value:Boolean){
        sharedPreferencesManager.setBoolean(MY_KEY_SWITCHER,value)
    }
}