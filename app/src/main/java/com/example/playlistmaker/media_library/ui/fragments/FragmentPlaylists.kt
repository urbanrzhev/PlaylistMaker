package com.example.playlistmaker.media_library.ui.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.playlistmaker.common.util.BindingFragment
import com.example.playlistmaker.databinding.FragmentMediaLibraryHostBinding
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding

class FragmentPlaylists:BindingFragment<FragmentPlaylistsBinding>() {
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlaylistsBinding {
        return FragmentPlaylistsBinding.inflate(inflater, container, false)
    }
    companion object{
        fun newInstance()= FragmentPlaylists()
    }
}