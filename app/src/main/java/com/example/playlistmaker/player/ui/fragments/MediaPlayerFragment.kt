package com.example.playlistmaker.player.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
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
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaPlayerFragment : BindingFragment<FragmentAudioPlayerBinding>() {
    private lateinit var dataTrack: Track
    private val viewModel by viewModel<MediaPlayerViewModel>()
    private lateinit var bottomSheetBehavior:BottomSheetBehavior<LinearLayout>
    private val adapter = PlayerAdapter{
        viewModel.addTrackInPlaylist(it)
    }
    private lateinit var bottomSheetCallback:BottomSheetCallback

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
        bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback(){
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        overlay.visibility = View.GONE
                    }
                    else -> {
                        viewModel.getPlaylists()
                        overlay.visibility = View.VISIBLE
                    }
                }
                viewModel.stateBottomSheetBehavior(bottomSheetBehavior.state)
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        }
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer)
        bottomSheetBehavior.addBottomSheetCallback(bottomSheetCallback)
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
        viewModel.observeShowMessage.observe(viewLifecycleOwner){ pair ->
            var s: String
            if(pair.first)
                s = getString(R.string.success_add_playlist)
            else
                s = getString(R.string.before_add_playlist)
            s+= " "+pair.second
            showToast(s)
        }
        viewModel.observeBottomSheetBehaviorState.observe(viewLifecycleOwner){ state ->
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
            bottomSheetBehavior.removeBottomSheetCallback(bottomSheetCallback)
            viewModel.stateBottomSheetBehavior(BottomSheetBehavior.STATE_HIDDEN)
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

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val ARGS_KEY_TRACK = "track"
        fun createArgs(track: Track): Bundle = TrackBundleUtil.createBundle(ARGS_KEY_TRACK, track)
    }
}