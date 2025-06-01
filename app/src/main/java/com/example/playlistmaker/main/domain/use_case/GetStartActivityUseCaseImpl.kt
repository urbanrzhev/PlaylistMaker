package com.example.playlistmaker.main.domain.use_case

import com.example.playlistmaker.common.domain.api.SharedPreferencesManager
import com.example.playlistmaker.main.domain.api.GetStartActivityUseCase

class GetStartActivityUseCaseImpl(private val sharedPreferencesManager: SharedPreferencesManager):GetStartActivityUseCase {
    override fun execute():Boolean{
        return sharedPreferencesManager.getBoolean(MY_KEY_ON_OFF_MEDIA_LIBRARY_ACTIVITY)
    }
    companion object{
        private const val MY_KEY_ON_OFF_MEDIA_LIBRARY_ACTIVITY = "my_key_on_off_media_library_activity"
    }
}