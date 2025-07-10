package com.example.playlistmaker.media_library.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.common.util.SingleLiveEvent
import com.example.playlistmaker.media_library.domain.api.GetStartFragmentUseCase
import com.example.playlistmaker.media_library.domain.api.SetStartFragmentUseCase

class MediaLibraryViewModel(
    private val getStartFragmentUseCase: GetStartFragmentUseCase,
    private val setStartFragmentUseCase: SetStartFragmentUseCase
) : ViewModel() {
    private val startFragment = SingleLiveEvent<Int>()
    fun observeStartFragment(): LiveData<Int> = startFragment

    fun startFragment() {
        try{
            startFragment.value = getStartFragmentUseCase.execute()
        }catch (e:Exception){
            startFragment.value = 0
        }
    }

    fun setStartFragment(i: Int?) {
        setStartFragmentUseCase.execute(
            when (i) {
                0 -> FAVORITES_TRACKS_FRAGMENT
                else -> PLAYLISTS_FRAGMENT
            }
        )
    }

    companion object {
        private const val FAVORITES_TRACKS_FRAGMENT = "favorites_tracks"
        private const val PLAYLISTS_FRAGMENT = "playlists"
    }
}