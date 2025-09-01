package com.example.playlistmaker.info_playlist.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.common.domain.models.Playlist
import com.example.playlistmaker.common.ui.adapters_holder.TrackAdapter
import com.example.playlistmaker.common.util.BindingFragment
import com.example.playlistmaker.common.util.MyDisplayMetrics
import com.example.playlistmaker.common.util.PlaylistBundleUtil
import com.example.playlistmaker.databinding.FragmentInfoPlaylistBinding
import com.example.playlistmaker.info_playlist.ui.view_model.InfoPlaylistViewModel
import com.example.playlistmaker.player.ui.fragments.MediaPlayerFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class InfoPlaylistFragment:BindingFragment<FragmentInfoPlaylistBinding>() {
    private val viewModel by viewModel<InfoPlaylistViewModel>()
    private val backPressedCallback = object :OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            confirmDialog.show()
        }
    }
    private lateinit var confirmDialog:MaterialAlertDialogBuilder
    private val adapter = TrackAdapter{ track->
        findNavController().navigate(
            R.id.action_infoPlaylistFragment_to_mediaPlayerFragment,
            MediaPlayerFragment.createArgs(track)
        )
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentInfoPlaylistBinding {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,backPressedCallback)
        return FragmentInfoPlaylistBinding.inflate(inflater,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recycler.adapter = adapter
        confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.delete_track))
            .setNegativeButton(getString(R.string.not)) { _, _ -> }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                backPressedCallback.isEnabled = false
                navigateUp()
            }
        viewModel.setPlaylist(PlaylistBundleUtil.rewriteBundle(arguments?.getBundle(ARGS_PLAYLIST)!!))
        viewModel.observePlaylist.observe(viewLifecycleOwner){ playlist->
            Log.v("my",playlist.toString())
            showPlaylist(playlist)
        }
        binding.vectorBack.setOnClickListener {
            navigateUp()
        }
    }

    fun navigateUp(){
        requireActivity().onBackPressedDispatcher.onBackPressed()
    }

    fun showPlaylist(playlist:Playlist){
        binding.textName.text = playlist.name
        if(playlist.photo.isNotEmpty()){
            Glide.with(requireContext())
                .load(playlist.photo.toUri())
                .placeholder(R.drawable.placeholder)
                .into(binding.imageCover)
        }
        if(playlist.description.isNotEmpty()) {
            binding.textDescription.isVisible = true
            binding.textDescription.text = playlist.description
        }
    }

    companion object{
        private const val ARGS_PLAYLIST = "args_playlist"
        fun createArgs(playlist:Playlist):Bundle = PlaylistBundleUtil.createBundle(ARGS_PLAYLIST, playlist)
    }
}