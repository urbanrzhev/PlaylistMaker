package com.example.playlistmaker.modify_playlist.ui.fragments

import android.os.Bundle
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.common.domain.models.Playlist
import com.example.playlistmaker.common.util.BindingFragment
import com.example.playlistmaker.common.util.PlaylistBundleUtil
import com.example.playlistmaker.databinding.FragmentCreateAndModifyPlaylistBinding
import com.example.playlistmaker.modify_playlist.ui.view_models.ModifyPlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ModifyPlaylistFragment:BindingFragment<FragmentCreateAndModifyPlaylistBinding>() {
    private val viewModel by viewModel<ModifyPlaylistViewModel>()
    private var textWatcherName: TextWatcher? = null
    private var textWatcherDescription: TextWatcher? = null
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCreateAndModifyPlaylistBinding {
        return FragmentCreateAndModifyPlaylistBinding.inflate(inflater,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setPlaylist(arguments?.getString(ARGS_PLAYLIST_NAME)!!)
        setupObservers()
        setupClickListeners()
        binding.buttonCreate.text = getString(R.string.save)
        binding.fragmentName.text = getString(R.string.modify)
        textWatcherName = binding.editTextName.doOnTextChanged { text, _, _, _ ->
            viewModel.setName(text.toString())
        }
        textWatcherDescription = binding.editTextDescription.doOnTextChanged { text, _, _, _ ->
            viewModel.setDescription(text.toString())
        }
    }

    private fun setupObservers(){
        viewModel.observePlaylist.observe(viewLifecycleOwner){ playlist->
            showPlaylist(playlist)
        }
        viewModel.observeClose.observe(viewLifecycleOwner){ close ->
            if(close)
                navigateUp()
        }
        viewModel.observeButtonSaveEnabled.observe(viewLifecycleOwner){ enabled ->
            binding.buttonCreate.isEnabled = enabled
        }
    }

    private fun setupClickListeners(){
        binding.buttonCreate.setOnClickListener {
            viewModel.saveModifyPlaylist()
        }
    }

    private fun navigateUp() {
        Log.v("my","navigateUp")
        requireActivity().onBackPressedDispatcher.onBackPressed()
    }

    private fun showPlaylist(playlist: Playlist) {
        binding.editTextName.setText(playlist.name)
        if(playlist.photo.isNotEmpty()) {
            binding.imageFon.isVisible = false
            binding.imageCover.background = null
            Glide.with(requireContext())
                .load(playlist.photo)
                .placeholder(R.drawable.placeholder)
                .into(binding.imageCover)
        }
        if (playlist.description.isNotEmpty()) {
            binding.editTextDescription.setText(playlist.description)
            //binding.textDescription.text = playlist.description
        }
        //binding.behaviorPlaylistName.text = playlist.name
    }

    companion object {
        private const val ARGS_PLAYLIST_NAME = "args_playlist_name"
        fun createArgs(playlistName: String): Bundle =
            bundleOf(
                ARGS_PLAYLIST_NAME to playlistName
            )
    }
}