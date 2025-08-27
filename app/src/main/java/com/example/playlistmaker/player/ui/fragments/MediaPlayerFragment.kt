package com.example.playlistmaker.player.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.databinding.FragmentAudioPlayerBinding
import com.example.playlistmaker.common.util.BindingFragment
import com.example.playlistmaker.common.util.TrackBundleUtil
import com.example.playlistmaker.player.ui.adapter_holder.PlayerAdapter
import com.example.playlistmaker.player.ui.models.PlayerState
import com.example.playlistmaker.player.ui.view_model.MediaPlayerViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaPlayerFragment : BindingFragment<FragmentAudioPlayerBinding>() {
    private lateinit var dataTrack: Track
    private val viewModel by viewModel<MediaPlayerViewModel>()
    private lateinit var bottomSheetBehavior:BottomSheetBehavior<LinearLayout>
    private val adapter = PlayerAdapter{
        viewModel.updatePlaylist(it)
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAudioPlayerBinding {
        return FragmentAudioPlayerBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bottomSheetContainer = binding.playlistsBottomSheet
        val overlay  = binding.overlay
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer)
        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback(){
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                        viewModel.overlayVisible(false)
                    }
                    else -> {
                        viewModel.getPlaylists()
                        binding.overlay.visibility = View.VISIBLE
                        viewModel.overlayVisible(true)
                    }
                }
                viewModel.stateBottomSheetBehavior(bottomSheetBehavior.state)
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })
        dataTrack = TrackBundleUtil.rewriteBundle(requireArguments().getBundle(ARGS_KEY_TRACK))!!
        if(savedInstanceState == null)
            viewModel.initTrack(dataTrack)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.playerProgressFlow.collect { state ->
                    binding.textProgress.text = state
                }
            }
        }
        viewModel.observeOverlay.observe(viewLifecycleOwner){ visible ->
            overlay.isVisible = visible
        }
        viewModel.observeBottomSheetBehaviorState.observe(viewLifecycleOwner){ state ->
            if(state != BottomSheetBehavior.STATE_SETTLING && state != BottomSheetBehavior.STATE_DRAGGING)
                bottomSheetBehavior.state = state
        }
        viewModel.observeItemsAdapter.observe(viewLifecycleOwner){ list ->
            adapter.updateList(list)
        }
        viewModel.observePlayerState().observe(viewLifecycleOwner) { state ->
            renderUI(state)
        }
        binding.recycler.adapter = adapter
        binding.buttonNewPlaylist.setOnClickListener {
            findNavController().navigate(
                R.id.action_mediaPlayerFragment_to_newPlaylistFragment
            )
        }
        binding.buttonPause.setOnClickListener {
            viewModel.control()
        }

        binding.buttonAddPlayList.setOnClickListener {
            viewModel.stateBottomSheetBehavior(BottomSheetBehavior.STATE_COLLAPSED)
        }

        binding.vectorBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.buttonLike.setOnClickListener {
            viewModel.onFavoritesClicked(binding.buttonLikeYes.isVisible)
            binding.buttonLikeYes.isVisible = !binding.buttonLikeYes.isVisible
        }
        loadTrack(dataTrack)
        viewModel.getPlaylists()
    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }

    private fun renderUI(value: PlayerState) {
        binding.buttonPause.isEnabled = value.isPlayButtonEnabled
        binding.buttonPause.setBackgroundResource(value.playButtonBackground)
    }

    private fun loadTrack(model: Track) {
        ShowActiveTrack(requireContext(), binding, model)
            .show()
    }

    companion object {
        private const val ARGS_KEY_TRACK = "track"
        fun createArgs(track: Track): Bundle = TrackBundleUtil.createBundle(ARGS_KEY_TRACK, track)
    }
}