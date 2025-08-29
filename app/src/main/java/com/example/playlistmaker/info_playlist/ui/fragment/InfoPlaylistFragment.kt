package com.example.playlistmaker.info_playlist.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.playlistmaker.common.domain.models.Playlist
import com.example.playlistmaker.common.util.BindingFragment
import com.example.playlistmaker.common.util.PlaylistBundleUtil
import com.example.playlistmaker.databinding.FragmentInfoPlaylistBinding
import com.example.playlistmaker.info_playlist.ui.view_model.InfoPlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class InfoPlaylistFragment:BindingFragment<FragmentInfoPlaylistBinding>() {
    private val viewModel by viewModel<InfoPlaylistViewModel>()
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentInfoPlaylistBinding {
        return FragmentInfoPlaylistBinding.inflate(inflater,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.observePlaylist.observe(viewLifecycleOwner){ playlist->
            Log.v("my",playlist.toString())
        }
    }

    companion object{
        private const val ARGS_PLAYLIST = "args_playlist"
        fun createArgs(playlist:Playlist):Bundle = PlaylistBundleUtil.createBundle(ARGS_PLAYLIST, playlist)
    }
}