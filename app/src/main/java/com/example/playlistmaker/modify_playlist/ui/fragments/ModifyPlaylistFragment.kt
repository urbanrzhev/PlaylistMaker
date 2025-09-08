package com.example.playlistmaker.modify_playlist.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.modify_playlist.ui.view_models.ModifyPlaylistViewModel
import com.example.playlistmaker.new_playlist.ui.fragments.NewPlaylistFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class ModifyPlaylistFragment : NewPlaylistFragment() {
    override val viewModel by viewModel<ModifyPlaylistViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonCreate.text = getString(R.string.save)
        binding.fragmentName.text = getString(R.string.modify)
        viewModel.setPlaylist(arguments?.getLong(ARGS_PLAYLIST_ID) ?: 0L)
        viewModel.observeShowPlaylist.observe(viewLifecycleOwner) { playlist ->
            binding.editTextName.setText(playlist.name)
            binding.editTextDescription.setText(playlist.description)
        }
        binding.buttonCreate.setOnClickListener {
            viewModel.updatePlaylist()
        }
        binding.vectorBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun attachBackPressedDispatcher() {}

    companion object {
        private const val ARGS_PLAYLIST_ID = "args_playlist_id"
        fun createArgs(playlistId: Long): Bundle =
            bundleOf(
                ARGS_PLAYLIST_ID to playlistId
            )
    }
}