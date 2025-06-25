package com.example.playlistmaker.media_library.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.common.domain.api.SetStartActivityUseCase
import com.example.playlistmaker.media_library.domain.api.GetStartFragmentUseCase
import com.example.playlistmaker.media_library.domain.api.SetStartFragmentUseCase

class MediaLibraryViewModel(
    private val setStartActivityUseCase: SetStartActivityUseCase,
    private val getStartFragmentUseCase: GetStartFragmentUseCase,
    private val setStartFragmentUseCase: SetStartFragmentUseCase
) : ViewModel() {
    private val startFragment = MutableLiveData(0)
    fun observeStartFragment(): LiveData<Int> = startFragment
    fun setStartActivity() {
        setStartActivityUseCase.execute(MEDIA_LIBRARY_ACTIVITY)
    }

    fun getStartFragment() {
        startFragment.value = getStartFragmentUseCase.execute()
    }

    fun setStartFragment(i: Int) {
        setStartFragmentUseCase.execute(
            when (i) {
                0 -> FAVORITES_TRACKS_FRAGMENT
                1 -> PLAYLISTS_FRAGMENT
                else -> FAVORITES_TRACKS_FRAGMENT
            }
        )
    }

    companion object {
        private const val FAVORITES_TRACKS_FRAGMENT = "favorites_tracks"
        private const val PLAYLISTS_FRAGMENT = "playlists"
        private const val MEDIA_LIBRARY_ACTIVITY = "media_library"
    }
}