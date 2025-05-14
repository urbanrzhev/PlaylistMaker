package com.example.playlistmaker.domain.use_case

import com.example.playlistmaker.domain.api.SharedPreferencesManager

private const val MY_KEY_SWITCHER = "my_key_switcher"
class GetThemeUseCase(private val sharedPreferencesManager: SharedPreferencesManager) {
    fun getTheme(callback:(Boolean)->Unit){
        callback.invoke(sharedPreferencesManager.getBoolean(MY_KEY_SWITCHER))
    }
}