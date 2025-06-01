package com.example.playlistmaker.search.domain.use_case

import com.example.playlistmaker.common.domain.api.SharedPreferencesManager
import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.search.domain.api.SetActiveTrackUseCase

class SetActiveTrackUseCaseImpl(private val sharedPreferencesManager: SharedPreferencesManager):
    SetActiveTrackUseCase {
    override fun execute(track: Track){
        sharedPreferencesManager.setTrack(MY_KEY_DEFAULT_TRACK_MEDIA_LIBRARY_ACTIVITY,track)
    }
    companion object{
        private const val MY_KEY_DEFAULT_TRACK_MEDIA_LIBRARY_ACTIVITY =
            "my_key_default_track_media_library_activity"
    }
}