package com.example.playlistmaker.app.domain.use_case

import com.example.playlistmaker.app.domain.api.SetThemeUseCase
import com.example.playlistmaker.common.domain.api.SharedPreferencesManager

class SetThemeUseCaseImpl(private val sharedPreferencesManager: SharedPreferencesManager):SetThemeUseCase {

    override fun execute(value:Boolean){
        sharedPreferencesManager.setBoolean(MY_KEY_SWITCHER,value)
    }
    companion object{
        private const val MY_KEY_SWITCHER = "my_key_switcher"
    }
}