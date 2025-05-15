package com.example.playlistmaker.domain.use_case

import com.example.playlistmaker.domain.api.SharedPreferencesManager

private const val MY_KEY_ON_OFF_MEDIA_LIBRARY_ACTIVITY = "my_key_on_off_media_library_activity"
class SetStartActivityUseCase(private val sharedPreferencesManager: SharedPreferencesManager) {
    fun setActivity(value:Boolean){
        sharedPreferencesManager.setBoolean(MY_KEY_ON_OFF_MEDIA_LIBRARY_ACTIVITY,value)
    }
}