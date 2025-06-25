package com.example.playlistmaker.media_library.domain.use_case

import com.example.playlistmaker.common.domain.api.SharedPreferencesManager
import com.example.playlistmaker.media_library.domain.api.GetStartFragmentUseCase

class GetStartFragmentUseCaseImpl(
    private val sharedPreferences: SharedPreferencesManager
) : GetStartFragmentUseCase {
    override fun execute(): Int = when (sharedPreferences.getString(MY_KEY_START_FRAGMENTS)) {
        FAVORITES_TRACKS_FRAGMENT -> 0
        PLAYLISTS_FRAGMENT -> 1
        else -> 0
    }

    companion object {
        private const val MY_KEY_START_FRAGMENTS = "my_key_start_fragments"
        private const val FAVORITES_TRACKS_FRAGMENT = "favorites_tracks"
        private const val PLAYLISTS_FRAGMENT = "playlists"
    }
}