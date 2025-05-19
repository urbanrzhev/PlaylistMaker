package com.example.playlistmaker.domain.use_case

import com.example.playlistmaker.domain.api.SharedPreferencesManager
import com.example.playlistmaker.domain.models.Track

private const val MY_KEY_DEFAULT_TRACK_MEDIA_LIBRARY_ACTIVITY =
    "my_key_default_track_media_library_activity"
class GetActiveTrackUseCase(private val sharedPreferencesManager: SharedPreferencesManager) {
    fun getTrack(callback:(Track)->Unit){
        callback.invoke(sharedPreferencesManager.getTrack(MY_KEY_DEFAULT_TRACK_MEDIA_LIBRARY_ACTIVITY))
    }
}