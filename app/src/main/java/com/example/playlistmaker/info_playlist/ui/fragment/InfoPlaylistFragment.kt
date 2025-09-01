package com.example.playlistmaker.info_playlist.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import kotlinx.coroutines.channels.ticker
import org.koin.androidx.viewmodel.ext.android.viewModel

class InfoPlaylistFragment : BindingFragment<FragmentInfoPlaylistBinding>() {
    private val viewModel by viewModel<InfoPlaylistViewModel>()
    private lateinit var confirmDialog: MaterialAlertDialogBuilder
    private val adapter = TrackAdapter(longCallback = { track ->
        confirmDialog
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                viewModel.deleteTrack(track)
            }
        confirmDialog.show()
    },
        callback = { track ->
            findNavController().navigate(
                R.id.action_infoPlaylistFragment_to_mediaPlayerFragment,
                MediaPlayerFragment.createArgs(track)
            )
        }
    )

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentInfoPlaylistBinding {
        return FragmentInfoPlaylistBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recycler.adapter = adapter
        confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.delete_track))
            .setNegativeButton(getString(R.string.not)) { _, _ -> }
        viewModel.createPlaylist(PlaylistBundleUtil.rewriteBundle(arguments?.getBundle(ARGS_PLAYLIST)!!))
        viewModel.observePlaylist.observe(viewLifecycleOwner) { playlist ->
            showPlaylist(playlist)
        }
        viewModel.observeTracks.observe(viewLifecycleOwner) { list ->
            adapter.updateList(list)
        }
        viewModel.observeTotalTime.observe(viewLifecycleOwner){ totalTime ->
            binding.textCommonLength.text = totalTime
        }
        viewModel.observeTotalTracks.observe(viewLifecycleOwner) { totalTracks ->
            if(totalTracks.isNotEmpty()) {
                binding.groupTexts.isVisible = true
                binding.textTracks.text = totalTracks
            }else
                binding.groupTexts.isVisible = false
        }
        binding.shared.setOnClickListener {
            if(viewModel.getSizeTrackList() == 0)
                Toast.makeText(requireContext(),getString(R.string.error_message_toast), Toast.LENGTH_SHORT).show()
            else {}

        }
        binding.vectorBack.setOnClickListener {
            navigateUp()
        }
    }

    fun navigateUp() {
        requireActivity().onBackPressedDispatcher.onBackPressed()
    }

    fun showPlaylist(playlist: Playlist) {
        binding.textName.text = playlist.name
        if (playlist.photo.isNotEmpty()) {
            Glide.with(requireContext())
                .load(playlist.photo.toUri())
                .placeholder(R.drawable.placeholder)
                .into(binding.imageCover)
        }
        if (playlist.description.isNotEmpty()) {
            binding.textDescription.isVisible = true
            binding.textDescription.text = playlist.description
        }
    }

    companion object {
        private const val ARGS_PLAYLIST = "args_playlist"
        fun createArgs(playlist: Playlist): Bundle =
            PlaylistBundleUtil.createBundle(ARGS_PLAYLIST, playlist)
    }
}