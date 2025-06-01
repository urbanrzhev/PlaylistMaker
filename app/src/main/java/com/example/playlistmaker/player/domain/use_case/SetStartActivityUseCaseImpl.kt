package com.example.playlistmaker.player.domain.use_case

import com.example.playlistmaker.common.domain.api.SharedPreferencesManager
import com.example.playlistmaker.player.domain.api.SetStartActivityUseCase

class SetStartActivityUseCaseImpl(private val sharedPreferencesManager: SharedPreferencesManager):SetStartActivityUseCase {
    override fun execute(value:Boolean){
        sharedPreferencesManager.setBoolean(MY_KEY_ON_OFF_MEDIA_LIBRARY_ACTIVITY,value)
    }
    companion object{
        private const val MY_KEY_ON_OFF_MEDIA_LIBRARY_ACTIVITY = "my_key_on_off_media_library_activity"
    }
}