package com.example.playlistmaker.media_library.domain.use_case

import com.example.playlistmaker.common.domain.api.SharedPreferencesManager
import com.example.playlistmaker.media_library.domain.api.SetStartFragmentUseCase

class SetStartFragmentUseCaseImpl(
    private val sharedPreferences: SharedPreferencesManager
): SetStartFragmentUseCase {
    override fun execute(data: String) {
        sharedPreferences.setString(MY_KEY_START_FRAGMENTS,data)
    }
    companion object{
        private const val MY_KEY_START_FRAGMENTS = "my_key_start_fragments"
    }
}