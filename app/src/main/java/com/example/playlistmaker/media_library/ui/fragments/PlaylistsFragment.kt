package com.example.playlistmaker.media_library.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.common.util.BindingFragment
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.info_playlist.ui.fragment.InfoPlaylistFragment
import com.example.playlistmaker.media_library.ui.adapter_holder.MediaLibraryPlaylistAdapter
import com.example.playlistmaker.media_library.ui.models.RecyclerState
import com.example.playlistmaker.media_library.ui.view_model.PlaylistsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : BindingFragment<FragmentPlaylistsBinding>() {
    private val viewModel: PlaylistsViewModel by viewModel()
    private val adapter = MediaLibraryPlaylistAdapter { playlist ->
        findNavController().navigate(
            R.id.action_mediaLibraryFragment_to_infoPlaylistFragment,
            InfoPlaylistFragment.createArgs(playlist.playlistId)
        )
    }
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlaylistsBinding {
        return FragmentPlaylistsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.playlistState.observe(viewLifecycleOwner) {
            when (it) {
                is RecyclerState.Success -> {
                    adapter.updateList(it.data)
                    renderUi(false)
                }
                is RecyclerState.Error -> {
                    renderUi(true)
                }
                is RecyclerState.Idle -> {
                    binding.group.isVisible = false
                    binding.recycler.isVisible = false
                }
            }
        }
        binding.recycler.adapter = adapter
        binding.recycler.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.button.setOnClickListener {
            findNavController().navigate(
                R.id.action_mediaLibraryFragment_to_newPlaylistFragment
            )
        }
        viewModel.loadDatabase()
    }

    private fun renderUi(value: Boolean) {
        binding.group.isVisible = value
        binding.recycler.isVisible = !value
    }

    companion object {
        fun newInstance() = PlaylistsFragment()
    }
}