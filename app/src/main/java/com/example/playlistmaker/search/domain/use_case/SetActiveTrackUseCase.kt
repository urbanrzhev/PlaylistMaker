package com.example.playlistmaker.search.domain.use_case

import com.example.playlistmaker.common.domain.api.SharedPreferencesManager
import com.example.playlistmaker.common.domain.models.Track

class SetActiveTrackUseCase(private val sharedPreferencesManager: SharedPreferencesManager) {
    companion object{
        private const val MY_KEY_DEFAULT_TRACK_MEDIA_LIBRARY_ACTIVITY =
            "my_key_default_track_media_library_activity"
    }
    fun execute(track: Track){
        sharedPreferencesManager.setTrack(MY_KEY_DEFAULT_TRACK_MEDIA_LIBRARY_ACTIVITY,track)
    }
}