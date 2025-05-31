package com.example.playlistmaker.app.domain.use_case

import com.example.playlistmaker.common.domain.api.SharedPreferencesManager

class SetThemeUseCase(private val sharedPreferencesManager: SharedPreferencesManager) {
    companion object{
        private const val MY_KEY_SWITCHER = "my_key_switcher"
    }
    fun execute(value:Boolean){
        sharedPreferencesManager.setBoolean(MY_KEY_SWITCHER,value)
    }
}