package com.example.playlistmaker.player.domain.use_case

import com.example.playlistmaker.common.domain.api.SharedPreferencesManager

class SetStartActivityUseCase(private val sharedPreferencesManager: SharedPreferencesManager) {
    companion object{
        private const val MY_KEY_ON_OFF_MEDIA_LIBRARY_ACTIVITY = "my_key_on_off_media_library_activity"
    }
    fun setActivity(value:Boolean){
        sharedPreferencesManager.setBoolean(MY_KEY_ON_OFF_MEDIA_LIBRARY_ACTIVITY,value)
    }
}