package com.example.playlistmaker.common.domain.impl

import com.example.playlistmaker.common.domain.api.GetThemeUseCase
import com.example.playlistmaker.common.domain.api.SharedPreferencesManager

class GetThemeUseCaseImpl(private val sharedPreferencesManager: SharedPreferencesManager):GetThemeUseCase {
    override fun execute():Boolean{
        return sharedPreferencesManager.getBoolean(MY_KEY_SWITCHER)
    }
    companion object{
        private const val MY_KEY_SWITCHER = "my_key_switcher"
    }
}