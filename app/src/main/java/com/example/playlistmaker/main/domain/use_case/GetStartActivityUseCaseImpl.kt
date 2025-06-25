package com.example.playlistmaker.main.domain.use_case

import com.example.playlistmaker.main.domain.api.GetStartActivityUseCase
import com.example.playlistmaker.common.domain.api.SharedPreferencesManager

class GetStartActivityUseCaseImpl(
    private val sharedPreferences: SharedPreferencesManager
): GetStartActivityUseCase {
    override fun execute(): String =
        sharedPreferences.getString(MY_KEY_START_ACTIVITY)
    companion object{
        private const val MY_KEY_START_ACTIVITY = "my_key_start_activity"
    }
}