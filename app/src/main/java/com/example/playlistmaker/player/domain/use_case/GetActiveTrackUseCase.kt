package com.example.playlistmaker.player.domain.use_case

import com.example.playlistmaker.common.domain.api.SharedPreferencesManager
import com.example.playlistmaker.common.domain.models.Track

class GetActiveTrackUseCase(private val sharedPreferencesManager: SharedPreferencesManager) {
    companion object{
        private const val MY_KEY_DEFAULT_TRACK_MEDIA_LIBRARY_ACTIVITY =
            "my_key_default_track_media_library_activity"
    }
    fun execute():Track{
        return sharedPreferencesManager.getTrack(
            MY_KEY_DEFAULT_TRACK_MEDIA_LIBRARY_ACTIVITY
        )
    }
}