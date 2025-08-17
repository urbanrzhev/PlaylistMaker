package com.example.playlistmaker.player.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.common.ui.view_model.SharedViewModel
import com.example.playlistmaker.databinding.FragmentAudioPlayerBinding
import com.example.playlistmaker.common.util.BindingFragment
import com.example.playlistmaker.common.util.TrackBundleUtil
import com.example.playlistmaker.player.ui.models.PlayerState
import com.example.playlistmaker.player.ui.view_model.MediaPlayerViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaPlayerFragment : BindingFragment<FragmentAudioPlayerBinding>() {
    private lateinit var dataTrack: Track
    private val viewModel by viewModel<MediaPlayerViewModel>()
    private val viewModelCommunicator by viewModel<SharedViewModel>()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAudioPlayerBinding {
        return FragmentAudioPlayerBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        viewModel.observePlayerState().observe(viewLifecycleOwner) { state ->
            renderUI(state)
        }

        binding.buttonPause.setOnClickListener {
            viewModel.control()
        }

        binding.vectorBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.buttonLike.setOnClickListener {
            viewModel.onFavoritesClicked(binding.buttonLikeYes.isVisible)
            binding.buttonLikeYes.isVisible = !binding.buttonLikeYes.isVisible
            viewModelCommunicator.setChange(dataTrack.trackId, binding.buttonLikeYes.isVisible)
        }
        loadTrack(dataTrack)
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