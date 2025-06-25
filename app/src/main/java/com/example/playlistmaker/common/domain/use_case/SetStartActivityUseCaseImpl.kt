package com.example.playlistmaker.common.domain.use_case

import com.example.playlistmaker.common.domain.api.SharedPreferencesManager
import com.example.playlistmaker.common.domain.api.SetStartActivityUseCase

class SetStartActivityUseCaseImpl(private val sharedPreferencesManager: SharedPreferencesManager):
    SetStartActivityUseCase {
    override fun execute(data:String){
        sharedPreferencesManager.setString(MY_KEY_START_ACTIVITY,data)
    }
    companion object{
        private const val MY_KEY_START_ACTIVITY = "my_key_start_activity"
    }
}