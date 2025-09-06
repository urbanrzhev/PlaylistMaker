package com.example.playlistmaker.info_playlist.ui.fragment

import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.example.playlistmaker.R
import com.example.playlistmaker.common.domain.models.Playlist
import com.example.playlistmaker.common.ui.adapters_holder.TrackAdapter
import com.example.playlistmaker.common.util.BindingFragment
import com.example.playlistmaker.databinding.FragmentInfoPlaylistBinding
import com.example.playlistmaker.info_playlist.ui.view_model.InfoPlaylistViewModel
import com.example.playlistmaker.modify_playlist.ui.fragments.ModifyPlaylistFragment
import com.example.playlistmaker.player.ui.fragments.MediaPlayerFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class InfoPlaylistFragment : BindingFragment<FragmentInfoPlaylistBinding>() {
    private val viewModel by viewModel<InfoPlaylistViewModel>()
    private lateinit var confirmDialog: MaterialAlertDialogBuilder
    private var clickedTrackSheetBehavior:Boolean = true
    private lateinit var thisPlaylist:Playlist
    private lateinit var menuSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var menuSheetCallback: BottomSheetCallback
    private val adapter = TrackAdapter(longCallback = { track ->
        if(checkClickedTrackSheetBehavior()) {
            confirmDialog
                .setTitle(getString(R.string.delete_track))
                .setPositiveButton(getString(R.string.yes)) { _, _ ->
                    viewModel.deleteTrack(track)
                }
            confirmDialog.show()
        }
    },
        callback = { track ->
            if(checkClickedTrackSheetBehavior()) {
                findNavController().navigate(
                    R.id.action_infoPlaylistFragment_to_mediaPlayerFragment,
                    MediaPlayerFragment.createArgs(track)
                )
            }
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
        setupBottomSheetBehavior()
        binding.recycler.adapter = adapter
        confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setNegativeButton(getString(R.string.not)) { _, _ -> }
        //thisPlaylist = PlaylistBundleUtil.rewriteBundle(arguments?.getBundle(ARGS_PLAYLIST)!!)
        viewModel.createPlaylist(arguments?.getLong(ARGS_PLAYLIST_ID)!!)
        setupObservers()
        setupClickListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.setMenuBehaviorState(menuSheetBehavior.state)
    }

    private fun setupBottomSheetBehavior(){
        val overlay = binding.overlay
        val menuSheetContainer = binding.menuBottomSheet
        menuSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    overlay.isVisible = false
                    clickedTrackSheetBehavior = true
                }
                else {
                    overlay.isVisible = true
                    clickedTrackSheetBehavior = false
                }
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        }
        menuSheetBehavior = BottomSheetBehavior.from(menuSheetContainer)
        menuSheetBehavior.addBottomSheetCallback(menuSheetCallback)
    }

    private fun setupObservers(){
        viewModel.observePlaylist.observe(viewLifecycleOwner) { playlist ->
            thisPlaylist = playlist
            showPlaylist(playlist)
        }
        viewModel.observeClose.observe(viewLifecycleOwner) { close ->
            if(close)
                navigateUp()
        }
        viewModel.observeTracks.observe(viewLifecycleOwner) { list ->
            adapter.updateList(list)
        }
        viewModel.observeTotalTime.observe(viewLifecycleOwner) { totalTime ->
            binding.textCommonLength.text = totalTime
        }
        viewModel.observeTotalTracks.observe(viewLifecycleOwner) { totalTracks ->
            binding.behaviorTracksCount.text = totalTracks
            if (totalTracks.isNotEmpty()) {
                binding.groupTexts.isVisible = true
                binding.textTracks.text = totalTracks
            } else
                binding.groupTexts.isVisible = false
        }
        viewModel.observeMenuBehaviorState.observe(viewLifecycleOwner) { state ->
            menuSheetBehavior.state = state
            if(state != BottomSheetBehavior.STATE_HIDDEN)
                binding.overlay.isVisible = true
        }
    }

    private fun setupClickListeners(){
        binding.buttonModifyPlaylist.setOnClickListener {
            //viewModel.setBehaviorControllerState(BehaviorControllerState.BehaviorMenu)
            menuSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            //renderBehavior(false)
        }
        binding.sharedBehavior2.setOnClickListener {

        }
        binding.modifyBehavior2.setOnClickListener {
            findNavController().navigate(R.id.action_infoPlaylistFragment_to_modifyPlaylistFragment,
                ModifyPlaylistFragment.createArgs(thisPlaylist.playlistId))
        }
        binding.deleteBehavior2.setOnClickListener {
            //viewModel.setMBehaviorControllerState(BehaviorControllerState.BehaviorTracks)
            confirmDialog
                .setTitle(getString(R.string.are_you_delete_playlist))
                .setPositiveButton(getString(R.string.yes)) { _, _ ->
                    viewModel.deletePlaylist()
                }
                .show()
        }
        binding.shared.setOnClickListener {
            if (viewModel.getSizeTrackList() == 0)
                Toast.makeText(
                    requireContext(),
                    getString(R.string.error_message_toast),
                    Toast.LENGTH_SHORT
                ).show()
            else {
            }

        }
        binding.vectorBack.setOnClickListener {
            navigateUp()
        }
    }

    private fun navigateUp() {
        requireActivity().onBackPressedDispatcher.onBackPressed()
    }

    private fun checkClickedTrackSheetBehavior() = clickedTrackSheetBehavior

    /*private fun renderBehavior(value: Boolean) {
        binding.tracksBottomSheet.isVisible = value
        binding.menuBottomSheet.isVisible = !value
        binding.overlay.isVisible = !value
    }*/

    private fun showPlaylist(playlist: Playlist) {
        binding.textName.text = playlist.name
        glide(playlist.photo.toUri())
            .into(binding.imageCover)
        glide(playlist.photo.toUri())
            .into(binding.imageBehavior)
        if (playlist.description.isNotEmpty()) {
            binding.textDescription.isVisible = true
            binding.textDescription.text = playlist.description
        }
        binding.behaviorPlaylistName.text = playlist.name
    }

    private fun glide(photo: Uri): RequestBuilder<Drawable> {
        return Glide.with(requireContext())
            .load(photo)
            .placeholder(R.drawable.placeholder)
    }

    companion object {
        private const val ARGS_PLAYLIST_ID = "args_playlist_id"
        fun createArgs(playlistId:Long): Bundle =
            bundleOf(
                ARGS_PLAYLIST_ID to playlistId
            )
    }
}