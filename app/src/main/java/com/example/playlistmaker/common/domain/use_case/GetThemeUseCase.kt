package com.example.playlistmaker.common.domain.use_case

import com.example.playlistmaker.common.domain.api.SharedPreferencesManager

class GetThemeUseCase(private val sharedPreferencesManager: SharedPreferencesManager) {
    companion object{
        private const val MY_KEY_SWITCHER = "my_key_switcher"
    }
    fun execute():Boolean{
        return sharedPreferencesManager.getBoolean(MY_KEY_SWITCHER)
    }
}