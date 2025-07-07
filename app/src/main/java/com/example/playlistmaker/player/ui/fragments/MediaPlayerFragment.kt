package com.example.playlistmaker.player.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.databinding.FragmentAudioPlayerBinding
import com.example.playlistmaker.common.util.BindingFragment
import com.example.playlistmaker.player.ui.view_model.MediaPlayerViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaPlayerFragment : BindingFragment<FragmentAudioPlayerBinding>() {
    private val viewModel: MediaPlayerViewModel by viewModel()
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAudioPlayerBinding {
        return FragmentAudioPlayerBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.observeTimeProgressLiveData().observe(viewLifecycleOwner) { time ->
            binding.textProgress.text = time
        }
        viewModel.observeBackgroundPlayButton().observe(viewLifecycleOwner) { background ->
            binding.buttonPause.setBackgroundResource(background)
        }
        viewModel.observeEnablePlayButton().observe(viewLifecycleOwner) { enabled ->
            binding.buttonPause.isEnabled = enabled
        }
        binding.buttonPause.setOnClickListener {
            viewModel.control()
        }
        binding.vectorBack.setOnClickListener {
            findNavController().navigateUp()
        }
        loadTrack(viewModel.getActiveTrack())
    }

    override fun onPause() {
        super.onPause()
        binding.buttonPause.setBackgroundResource(R.drawable.play_play)
        viewModel.pause()
    }

    private fun loadTrack(model: Track) {
        ShowActiveTrack(requireContext(), binding, model)
            .show()
    }
}